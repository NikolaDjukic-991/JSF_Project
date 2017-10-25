/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.display.Nastup;
import com.corejsf.display.Satnica;
import db.Dan;
import db.DanId;
import db.Festival;
import db.Festivalgrupa;
import db.helpers.FestivalHelper;
import db.Izvodjac;
import db.Komentar;
import db.Media;
import db.helpers.IzvodjacHelper;
import db.Nastupa;
import db.helpers.FestivalGrupeHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author John
 */
@Named(value = "importJSONBean")
@ViewScoped
public class ImportJSONBean implements Serializable {
    private static IzvodjacHelper izvodjachelper = new IzvodjacHelper();
    private static FestivalHelper festivalHelper = new FestivalHelper();
    private static FestivalGrupeHelper grupeHelper = new FestivalGrupeHelper();

    private boolean uploaded = false;
    private String msg ="";
    private List<Media> slike = new ArrayList<>();
    private List<Media> video = new ArrayList<>();
    private String videoUrl ="";
    private String buttonLabel="Dalje";
    private int korak = 0;
    private int cena = 0;
    private int rating = 5;
    
    
    private Festival fest = new Festival();
    
    @Inject
    private LoginBean loginBean;
    
    @Inject
    private NavBean navbean;

    @Inject
    private FestivalGrupeService festivalGrupeService;
    
    private List<Dan> dans = new ArrayList<>();
    private ArrayList<Satnica> satnica;
    
    public void upload(FileUploadEvent event){
        try{
            byte[] filecontent = event.getFile().getContents();
            SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy");
            String contentAsString = new String(filecontent);
            JSONObject json = new JSONObject(contentAsString.trim());        
            JSONObject festivaljson = json.getJSONObject("Festival");
            if(festivaljson != null){
                fest.setIme(festivaljson.getString("Name"));
                String festGrupa = festivaljson.getString("Group");
                List<Festivalgrupa> grupe = festivalGrupeService.getSveGrupe();
                Festivalgrupa selectedGrupa = null;
                for(Festivalgrupa grupa : grupe){
                    if(grupa.getOpis().equals(festGrupa)){
                        selectedGrupa = grupa;
                    }
                }
                if(selectedGrupa == null){
                    Festivalgrupa novaGrupa = grupeHelper.dodajFestivalGrupu(new Festivalgrupa(festGrupa));
                    selectedGrupa = novaGrupa;
                }
                fest.setFestivalgrupa(selectedGrupa);
                fest.setMesto(festivaljson.getString("Place"));
                String dateString = festivaljson.getString("StartDate");
                fest.setOd(dateformatter.parse(dateString));
                dateString = festivaljson.getString("EndDate");
                fest.setDo_(dateformatter.parse(dateString));
                if(fest.getOd().after(fest.getDo_())){
                    msg = "Datum pocetka ne moze biti nakon datuma kraja.";
                    FacesContext.getCurrentInstance().addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", msg));
                    return;
                } 
                
                long diff = fest.getDo_().getTime() - fest.getOd().getTime();
                int brdana = ((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))+1;

                JSONArray karteArray = festivaljson.getJSONArray("Tickets");

                if(brdana != karteArray.length()){
                    msg = "Broj dana i duzina niza sa brojem karata se ne poklapaju.";
                    FacesContext.getCurrentInstance().addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", msg));
                    return;
                }
                Set<Dan> dani = new HashSet<>();
                for(int i = 0; i < karteArray.length(); i++){
                    Dan d = new Dan();
                    d.setFestival(fest);
                    d.setBrKarata(karteArray.getInt(i));
                    d.setId(new DanId());
                    d.getId().setIdDan(i);
                    dani.add(d);
                }
                fest.setDans(dani);
                
                fest.setBrKarataPoDanu(festivaljson.getInt("TicketsPerDay"));
                fest.setBrKarataPoKorisniku(festivaljson.getInt("TicketsPerUser"));
                fest.setCenaDan(festivaljson.getInt("TicketPriceDay"));
                fest.setCenaPaket(festivaljson.getInt("TicketPriceFestival"));
                
                JSONArray izvojdaciArray = festivaljson.getJSONArray("PerformersList");
                for(int i = 0; i < izvojdaciArray.length(); i++){
                    JSONObject izvodjacObject = izvojdaciArray.getJSONObject(i);
                    String imeIzvodjaca = izvodjacObject.getString("Performer");
                    Izvodjac izvodjac = izvodjachelper.dohvatiIzvodjaca(imeIzvodjaca);
                    if(izvodjac == null){
                        izvodjachelper.dodajIzvodjaca(izvodjac = new Izvodjac(imeIzvodjaca));
                    }
                    String izvodjacStartDateStr = izvodjacObject.getString("StartDate");
                    Date izvodjacStart = dateformatter.parse(izvodjacStartDateStr);
                    String izvodjacStartTimeStr = izvodjacObject.getString("StartTime");
                    String izvodjacEndTimeStr = izvodjacObject.getString("EndTime");
                    
                    SimpleDateFormat timeformatter = new SimpleDateFormat("hh:mm:ss aaa");
                    Date izvodjacStartTime = timeformatter.parse(izvodjacStartTimeStr);
                    Date izvodjacEndTime = timeformatter.parse(izvodjacEndTimeStr);
                    
                    diff = izvodjacStart.getTime() - fest.getOd().getTime();
                    brdana = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    Dan danizvodjenja = null;
                    for(Dan d : fest.getDans()){
                        if(d.getId().getIdDan() == brdana){
                            Nastupa n = new Nastupa(null, null, izvodjac, izvodjacStartTime, izvodjacEndTime);
                            Set<Nastupa> nastupaSet = d.getNastupas();
                            nastupaSet.add(n);
                            d.setNastupas(nastupaSet);
                            break;
                        }
                    }
                }
                fest.setFacebook("");
                fest.setTwitter("");
                fest.setYoutube("");
                fest.setInstagram("");
                JSONArray socialArray = festivaljson.getJSONArray("SocialNetworks");
                for(int i = 0; i < socialArray.length(); i++){
                    JSONObject socialMediaObject = socialArray.getJSONObject(i);
                    String socialMediaName = socialMediaObject.getString("Name");
                    String socialMediaLink = socialMediaObject.getString("Link");
                    
                    if(socialMediaName.equalsIgnoreCase("Facebook")){
                        fest.setFacebook(socialMediaLink);
                    }
                    if(socialMediaName.equalsIgnoreCase("Twitter")){
                        fest.setTwitter(socialMediaLink);
                    }
                    if(socialMediaName.equalsIgnoreCase("YouTube")){
                        fest.setYoutube(socialMediaLink);
                    }
                    if(socialMediaName.equalsIgnoreCase("Instagram")){
                        fest.setInstagram(socialMediaLink);
                    }
                    
                }
                
                //festivalHelper.dodajFestival(fest);
                msg = "Uspesno ucitavanje festivala.";
                FacesContext.getCurrentInstance().addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspeh!", msg));
                uploaded = true;
            }
            
            
            
        } catch(JSONException e){
            e.printStackTrace();
            msg = "JSON Objekat neispravno formatiran.";
            FacesContext.getCurrentInstance().addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", msg));
        } catch(ParseException e) {
            e.printStackTrace();
            msg = "Greska prilikom parsiranja datuma.";
            FacesContext.getCurrentInstance().addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", msg));
        }

        
    }
    
    public void show(){
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(fest == null){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", "Festival ne postoji."));
        } else {
            navbean.showDetailsPreviewPage();
            
            dans = new ArrayList<>(fest.getDans());
            
            dans.sort((Dan d1, Dan d2)-> {
                return d1.getId().getIdDan() - d2.getId().getIdDan();
            });
            
            satnica = new ArrayList<>();
            for(Dan d : dans){
                Satnica s = new Satnica();
                ArrayList<Nastupa> nastupas = new ArrayList<>(d.getNastupas());
                nastupas.sort((Nastupa n1, Nastupa n2) -> {
                    return (int)(n1.getPocinje().getTime() - n2.getPocinje().getTime());
                });
                for(Nastupa n : nastupas){
                    String imeIzvodjaca = n.getIzvodjac().getIme();
                    Date od = n.getPocinje();
                    Date do_ = n.getZavrsava();
                    s.dodajNastup(new Nastup(od, do_, imeIzvodjaca));
                }
                
                satnica.add(s);
            }
            
            cena = fest.getCenaPaket();
            
            Set<Media> media;
            double rating = 0;
            int num = 0;
            
            
            for(Festival f : fest.getFestivalgrupa().getFestivals()){
                media = f.getMedias();
                for(Media m : media){
                    if(m.getOdobrena() == 1){
                        if(m.getTip() == 1 || m.getTip() == 3 ){
                            slike.add(m);
                        }
                        if(m.getTip() == 2 || m.getTip() == 4){
                            video.add(m);
                        }
                    }
                }
                
            }
            this.rating = (int) Math.round(rating/num);
            
        }
    }
    
    public void uploadSlike(FileUploadEvent event){
        FacesContext context = FacesContext.getCurrentInstance();
        UploadedFile file = event.getFile();
        String path = "d:/Nikola/NetBeansProjects/pia_add/uploads/"+fest.getIme();
        Path tempfile;
        try(InputStream istream = file.getInputstream()){
            Path folder;
            
            folder = Paths.get(path);
            if(!folder.toFile().exists()){
                folder.toFile().mkdir();
            }
            
            String filename = file.getFileName();
            String[] split = filename.split("\\.");
            String extension = "." + split[split.length-1];
            filename = filename.subSequence(0, filename.lastIndexOf('.')).toString();
            tempfile = Files.createTempFile(folder, filename, extension, new FileAttribute<?>[]{});
            
            
            Files.copy(istream, tempfile, StandardCopyOption.REPLACE_EXISTING);
            
            Media m = new Media();
            m.setFestival(fest);
            m.setOdobrena(1);
            m.setPath(tempfile.toAbsolutePath().toString());
            m.setTip(3);
            m.setUname(loginBean.getUname());
            slike.add(m);
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesan upload!", file.getFileName()));

        } catch (IOException ex) {
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Neuspesan upload!", file.getFileName()));
        }
    }
    
    public void uploadVideo(){
        FacesContext context = FacesContext.getCurrentInstance();
        String videoUrlFormatted = "https://www.youtube.com/v/";
        String videoUrlLowerCase = videoUrl.toLowerCase();
        Pattern regVariant1 = Pattern.compile("(http(s)?://)?(www\\.)?youtube.com/watch\\?v\\=[a-zA-Z0-9]+");
        Pattern regVariant2 = Pattern.compile("(http(s)?://)?(www\\.)?youtu.be/[a-zA-Z0-9]+");
        
        boolean matchFound = false;
        
        if( regVariant1.matcher(videoUrlLowerCase).matches() ){
            videoUrlFormatted = videoUrlFormatted.concat(videoUrl.substring(videoUrl.lastIndexOf('=')+1, videoUrl.length()));
            matchFound = true;
        }
        
        if( !matchFound && regVariant2.matcher(videoUrlLowerCase).matches() ){
            videoUrlFormatted = videoUrlFormatted.concat(videoUrl.substring(videoUrl.lastIndexOf('/')+1, videoUrl.length()));
            matchFound = true;
        }
        
        if(matchFound){
            Media m = new Media();
            m.setFestival(fest);
            m.setOdobrena(1);
            m.setPath(videoUrlFormatted);
            m.setTip(4);
            m.setUname(loginBean.getUname());
            video.add(m);
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesno dodavanje snimka!", videoUrl));
        } else {
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Neispravan format url-a!", videoUrl));
        }
    }
    
    public void dalje(){
        korak++;
        if(korak == 2){
            show();
        }
        if(korak == 3){
            List<Media> media = new ArrayList<>(slike);
            media.addAll(video);
            festivalHelper.dodajFestival(fest,media, dans);
            for(;korak > 0; korak--){
                reset(korak);
            }
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspeh", "Uspesno dodat festival!"));
        }
    }
    
    public void nazad(){
        reset(korak);
        korak--;
    }
    
    private void reset(int korak) {
        if(korak == 3){
            navbean.showImportFestival();
        }
        if(korak == 2){
            slike.clear();
            video.clear();

        }
        if(korak == 1){
            fest = new Festival();
        }
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public int getKorak() {
        return korak;
    }

    public String getMsg() {
        return msg;
    }

    public List<Media> getSlike() {
        return slike;
    }

    public void setSlike(List<Media> slike) {
        this.slike = slike;
    }

    public List<Media> getVideo() {
        return video;
    }

    public void setVideo(List<Media> video) {
        this.video = video;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public Festival getFest() {
        return fest;
    }

    public int getCena() {
        return cena;
    }

    public int getRating() {
        return rating;
    }

    public List<Dan> getDans() {
        return dans;
    }

    public ArrayList<Satnica> getSatnica() {
        return satnica;
    }

    
    
    
    
}
