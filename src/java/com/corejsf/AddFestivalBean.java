/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Dan;
import db.DanId;
import db.Festival;
import db.helpers.FestivalGrupeHelper;
import db.helpers.FestivalHelper;
import db.Festivalgrupa;
import db.Izvodjac;
import db.helpers.IzvodjacHelper;
import db.Media;
import db.Nastupa;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author John
 */
@ViewScoped
@Named("addFestivalBean")
public class AddFestivalBean implements Serializable {
    private int korak = 0;
    
    private Festival festival;
    
    private Izvodjac izabranIzvodjac = null;
    private List<Dan> dani = new ArrayList<>();
    private int brdana;
    private boolean dodajNovuGrupu = false;
    private String grupaOpis = "";
    private String noviIzvodjac;
    
    private Integer idgrupe = null;
    
    private Date datumNastupa, vremePocetkaNastupa, vremeKrajaNastupa;
    
    private boolean prikaziPanelZaNovuGrupu = false;
    private boolean prikaziPanelZaBiranjeGrupe = true;
    
    private Date danasnjiDatum = new Date();
    
    private List<Festivalgrupa> svegrupe;
    
    private static FestivalHelper helper = new FestivalHelper();
    private static FestivalGrupeHelper festivalGrupeHelper = new FestivalGrupeHelper();

    
    @Inject
    private FestivalGrupeService festivalGrupeService;
    
    @Inject
    private IzvodjaciService izvodjaciService;
    
    @Inject
    private LoginBean loginBean;
    
    private static IzvodjacHelper izvodjacHelper = new IzvodjacHelper();
    
    private List<Media> slike = new ArrayList<>();
    private List<Media> video = new ArrayList<>();
    
    private String buttonLabel = "Dalje";
    private String videoUrl = "";

    Nastupa n;
//    public void dodajFestival(){
//        Festivalgrupa fg = null;
//        if(dodajNovuGrupu){
//            fg = festivalGrupeHelper.dodajFestivalGrupu(new Festivalgrupa(grupaOpis));
//        } else {
//            fg = idgrupe;
//        }
//        
//        Set<Dan> daniset = new HashSet<>();
//        for(Dan d : dani){
//            daniset.add(new Dan(null, null, d.getBrKarata()));
//        }
//        
//        
//    }
    
    @PostConstruct
    public void init(){
        svegrupe = festivalGrupeService.getSveGrupe();
        festival = new Festival();
    }
    
    public void dodajGrupuPrekidac(){
        if(dodajNovuGrupu){
            prikaziPanelZaNovuGrupu = true;
            prikaziPanelZaBiranjeGrupe = false;
        } else {
            prikaziPanelZaNovuGrupu = false;
            prikaziPanelZaBiranjeGrupe = true;
        }
    }
    
    public void izabranOd(){
        
        
        
        if(festival.getOd() != null){
            if(festival.getDo_() != null){
                if(festival.getOd().after(festival.getDo_())) {
                    festival.setDo_(festival.getOd());
                    dani.add(new Dan(new DanId(0, 0), festival, 0, 0, 0));
                } else{
                    long diff = festival.getDo_().getTime() - festival.getOd().getTime();
                    brdana = ((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) +1;
                    int velicina = dani == null ? 0 : dani.size();
                    for(int i = velicina; i < brdana; i++){
                        dani.add( new Dan (new DanId(0, i), festival, 0, 0, 0) );
                    }
                    
                }
            }
        }
    }
    
    public void izabranDo(){
        
        if(festival.getOd() != null && festival.getDo_() != null){
            if(festival.getOd().after(festival.getDo_())) {
                festival.setOd(festival.getDo_());
                dani.clear();
                dani.add(new Dan(new DanId(0, 0), festival, 0, 0, 0));
            } else {
                long diff = festival.getDo_().getTime() - festival.getOd().getTime();
                brdana = ((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) +1;
                int velicina = dani == null ? 0 : dani.size();
                for(int i = velicina; i < brdana; i++){
                    dani.add( new Dan (new DanId(0, i), festival, 0, 0, 0) );
                }
            }
        }
    }
    
    public List<Izvodjac> completeIzvodjaci(String query) {
        List<Izvodjac> sviIzvodjaci = izvodjaciService.getIzvodjaci();
        List<Izvodjac> filtriraniIzvodjaci = new ArrayList<>();
         
        for (int i = 0; i < sviIzvodjaci.size(); i++) {
            Izvodjac izvodjac = sviIzvodjaci.get(i);
            if(izvodjac.getIme().toLowerCase().startsWith(query.toLowerCase())) {
                filtriraniIzvodjaci.add(izvodjac);
            }
        }
         
        return filtriraniIzvodjaci;
    }
    
    public void sacuvajVremeNastupa(){
        boolean found = false;
        long diff = datumNastupa.getTime() - festival.getOd().getTime();
        int brdana = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        Dan d = dani.get(brdana);
        if(d != null){
            Nastupa n = new Nastupa();
            n.setDan(d);
            n.setIzvodjac(izabranIzvodjac);
            n.setPocinje(vremePocetkaNastupa);
            n.setZavrsava(vremeKrajaNastupa);
            d.getNastupas().add(n);
            found = true;

        }
        
        if(found){
            datumNastupa = null;
            vremeKrajaNastupa = null;
            vremePocetkaNastupa = null;
            izabranIzvodjac = null;
            festival.setDans(new HashSet<>(dani.subList(0, brdana)));
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Nastup dodat!", "Uspesno dodavanje nastupa."));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Greska!", "Neuspesno dodavanje nastupa."));
        }

    }
    
    public void dalje(){
       korak++;
       if(korak == 1){
           if(dodajNovuGrupu){
               idgrupe = festivalGrupeHelper.dodajFestivalGrupu(new Festivalgrupa(grupaOpis)).getIdgrupe();
           }
       }
       if(korak == 5){
           buttonLabel = "Sacuvaj";
       }
       if(korak == 6){
           festival.setFestivalgrupa(festivalGrupeHelper.getFestivalGrupaWithId(idgrupe));
           List<Media> concatList = new ArrayList<>(slike);
           concatList.addAll(video);
           helper.dodajFestival(festival, concatList, dani);
           korak = 0;
           FacesContext context = FacesContext.getCurrentInstance();
           context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesno dodavanje festivala!", "Usepesno ste dodali festival " + festival.getIme() + "."));
           // reset all parameters
           festival = new Festival();
           slike = new ArrayList<>();
           video = new ArrayList<>();
           brdana = 0;
           buttonLabel = "Dalje";
           danasnjiDatum = new Date();
           dani = new ArrayList<>();
           datumNastupa = null;
           grupaOpis = null;
           idgrupe = null;
           izabranIzvodjac = null;
           n = null;
           noviIzvodjac = "";
           videoUrl = "";
           vremeKrajaNastupa = null;
           vremePocetkaNastupa = null;
       }
    }
    
    public void nazad(){
        resetPage(korak);
        korak--;
    }
    
    public void sacuvajIzvodjaca(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(noviIzvodjac != null && !noviIzvodjac.isEmpty()){
            if(izvodjacHelper.proveriJelPostoji(noviIzvodjac))
                context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Neuspesno dodavanje", "Izvodjac vec postoji."));
            else {
                izvodjacHelper.dodajIzvodjaca(izabranIzvodjac = new Izvodjac(noviIzvodjac));
                izvodjaciService.updateIzvodjaci();
                context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesno dodavanje", "Uspesno dodat izvodjac."));
            }
        } else {
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Neuspesno dodavanje", "Morate navesti ime izvodjaca."));
        }
        noviIzvodjac = "";
    }
    
    public void uploadSlike(FileUploadEvent event){
        FacesContext context = FacesContext.getCurrentInstance();
        UploadedFile file = event.getFile();
        String path = "d:/Nikola/NetBeansProjects/pia_add/uploads/"+festival.getIme();
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
            m.setFestival(festival);
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
            m.setFestival(festival);
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
    
    private void resetPage(int korak) {
        switch(korak){
            case 1:
                for(Dan d: dani){
                    d.setBrKarata(0);
                }
            break;
            case 2:
                izabranIzvodjac = null;
                datumNastupa = null;
                vremePocetkaNastupa = null;
                vremeKrajaNastupa = null;
                noviIzvodjac = null;
            break;
            case 3:
                festival.setFacebook("");
                festival.setTwitter("");
                festival.setInstagram("");
                festival.setYoutube("");
            break;
            case 4:
                slike.clear();
            break;
            case 5:
                video.clear();
            break;
            default:
                throw new RuntimeException("Greska u koracima");
        }
    }


    
    public List<Festivalgrupa> getSvegrupe() {
        return svegrupe;
    }

    public boolean isDodajNovuGrupu() {
        return dodajNovuGrupu;
    }

    public void setDodajNovuGrupu(boolean dodajNovuGrupu) {
        this.dodajNovuGrupu = dodajNovuGrupu;
    }

    public String getGrupaOpis() {
        return grupaOpis;
    }

    public void setGrupaOpis(String grupaOpis) {
        this.grupaOpis = grupaOpis;
    }

    public boolean isPrikaziPanelZaNovuGrupu() {
        return prikaziPanelZaNovuGrupu;
    }

    public boolean isPrikaziPanelZaBiranjeGrupe() {
        return prikaziPanelZaBiranjeGrupe;
    }

    public Date getDanasnjiDatum() {
        return danasnjiDatum;
    }

    

    public String getNoviIzvodjac() {
        return noviIzvodjac;
    }

    public void setNoviIzvodjac(String noviIzvodjac) {
        this.noviIzvodjac = noviIzvodjac;
    }


    public int getBrdana() {
        return brdana;
    }

    public void setBrdana(int brdana) {
        this.brdana = brdana;
    }

    public List<Dan> getDani() {
        return dani;
    }

    public void setDani(List<Dan> dani) {
        this.dani = dani;
    }

    public int getKorak() {
        return korak;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public Izvodjac getIzabranIzvodjac() {
        return izabranIzvodjac;
    }

    public void setIzabranIzvodjac(Izvodjac izabranIzvodjac) {
        this.izabranIzvodjac = izabranIzvodjac;
    }

    public Date getDatumNastupa() {
        return datumNastupa;
    }

    public void setDatumNastupa(Date datumNastupa) {
        this.datumNastupa = datumNastupa;
    }

    public Date getVremePocetkaNastupa() {
        return vremePocetkaNastupa;
    }

    public void setVremePocetkaNastupa(Date vremePocetkaNastupa) {
        this.vremePocetkaNastupa = vremePocetkaNastupa;
    }

    public Date getVremeKrajaNastupa() {
        return vremeKrajaNastupa;
    }

    public void setVremeKrajaNastupa(Date vremeKrajaNastupa) {
        this.vremeKrajaNastupa = vremeKrajaNastupa;
    }

    public Integer getIdgrupe() {
        return idgrupe;
    }

    public void setIdgrupe(Integer idgrupe) {
        this.idgrupe = idgrupe;
    }

    public List<Media> getSlike() {
        return slike;
    }



    public String getButtonLabel() {
        return buttonLabel;
    }

    public List<Media> getVideo() {
        return video;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    
    
    
    
}
