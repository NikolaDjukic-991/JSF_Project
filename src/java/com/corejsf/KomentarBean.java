/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Festival;
import db.Komentar;
import db.KomentarId;
import db.Media;
import db.MediaId;
import db.helpers.FestivalHelper;
import db.helpers.MediaHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author John
 */
@Named(value = "komentarBean")
@ViewScoped
public class KomentarBean implements Serializable {
    private static FestivalHelper helper = new FestivalHelper();
    private static MediaHelper mediahelper = new MediaHelper();
    
    @Inject
    private LoginBean loginBean;
    
    @Inject
    private FestivalDetailsBean festivalDetailsBean;
    
    
    
    private Integer ocena = 7;
    private String videolink;
    private String sadrzaj;
    private List<Media> slike = new ArrayList<>();
    private Festival festival;
    
    @PostConstruct
    public void init(){
        festival = festivalDetailsBean.getFest();
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
            m.setOdobrena(0);
            m.setPath(tempfile.toAbsolutePath().toString());
            m.setTip(1);
            m.setUname(loginBean.getUname());
            slike.add(m);
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesan upload!", file.getFileName()));

        } catch (IOException ex) {
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Neuspesan upload!", file.getFileName()));
        }
        
        
    }
    
    public void postavi(){
        boolean havecontent = false;
        Komentar k = new Komentar();
        if(ocena != null){
            k.setOcena(ocena);
            havecontent = true;
        }
        if(sadrzaj != null && !sadrzaj.isEmpty()){
            k.setTxt(sadrzaj);
            havecontent = true;
            festivalDetailsBean.getKomentars().add(0, k);
        }
        
        if(havecontent){
            k.setFestival(festival);
            k.setKorisnik(loginBean.getKorisnik());
            k.setId(new KomentarId(0, festival.getIdFest()));
            helper.addKomentar(k,festival);
        }
        if(videolink != null && !videolink.isEmpty()){
            Media m = new Media();
            m.setFestival(festival);
            m.setId(new MediaId());
            m.setOdobrena(0);
            m.setPath(videolink);
            m.setTip(2);
            m.setUname(loginBean.getUname());
            mediahelper.saveMedia(m);
        }
        
        if(!slike.isEmpty()){
            for(Media m : slike){
                festival.getMedias().add(m);
            }
            helper.updateMedias(slike, festival);
        }
        
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }
    
    
    
}
