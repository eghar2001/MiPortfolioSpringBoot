/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.argentinaPrograma.portfolio.controller;

import com.argentinaPrograma.portfolio.dto.EducacionDto;
import com.argentinaPrograma.portfolio.model.Educacion;
import com.argentinaPrograma.portfolio.model.Institucion;
import com.argentinaPrograma.portfolio.model.Perfil;
import com.argentinaPrograma.portfolio.model.TipoEducacion;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.argentinaPrograma.portfolio.service.IEducacionService;
import com.argentinaPrograma.portfolio.service.IInstitucionService;
import com.argentinaPrograma.portfolio.service.IPerfilService;
import com.argentinaPrograma.portfolio.service.ITipoEducacionService;
import com.argentinaPrograma.portfolio.service.PasaADto;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author nahux
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/educacion")
public  class EducacionController {
    @Autowired
    private IEducacionService eduServ;
    
    @Autowired
    private ITipoEducacionService tipoEduServ;
    
    @Autowired
    private IInstitucionService instiServ;
    
    @Autowired 
    private IPerfilService perfServ;
    
    @GetMapping("/traer")
    @ResponseBody
    public List<Educacion> getEducaciones(){
        return this.eduServ.getEducaciones();
    }
    
    @GetMapping("/traer/{id}")
    @ResponseBody
    public Educacion getEducacionById(@PathVariable Long id){
        return this.eduServ.getEducacionById(id);
    }
    
    @PostMapping("/crear")
    public EducacionDto createEducacion(@RequestBody EducacionDto eduDto){
        System.out.println(eduDto);      
       
       Educacion savedEdu = new Educacion();
       savedEdu.setDescripcion(eduDto.getDescripcion());
       savedEdu.setFechaDesde(eduDto.getFechaDesde());
       savedEdu.setFechaHasta(eduDto.getFechaHasta());
       
        /*
       Mapeamos los atributos que dependen de otras tablas
        */
       Perfil perf = this.perfServ.getPerfilById(eduDto.getIdPerfil());
       savedEdu.setPerfil(perf);
       
       TipoEducacion tipoEdu = this.tipoEduServ.getTipoEstudioById(eduDto.getIdTipoEdu());
       savedEdu.setTipoEducacion(tipoEdu);
       Institucion insti = buscaInsti(eduDto);
            savedEdu.setInstitucion(insti);
       
       savedEdu = this.eduServ.saveEducacion(savedEdu);
       
    return PasaADto.educacion(savedEdu);

       
       
        
    }
    
    @DeleteMapping("/borrar/{id}")
    public Long deleteEducacionById(@PathVariable Long id){      
      this.eduServ.deleteEducacionById(id);
      return id;
    }
    
    @PutMapping("/editar")
    public EducacionDto editEducacion( @RequestBody EducacionDto edittedEdu){
        Educacion savedEdu = this.eduServ.getEducacionById(edittedEdu.getId());
        savedEdu.setDescripcion(edittedEdu.getDescripcion());
        savedEdu.setFechaDesde(edittedEdu.getFechaDesde());
        savedEdu.setFechaHasta(edittedEdu.getFechaHasta());
        /*
        Chequeamos que el nombre no sea el mismo nombre, para ahorrarnos consultas a la BBDD
        */
        if(!savedEdu.getInstitucion().getNombre().equalsIgnoreCase(edittedEdu.getNombreInstitucion())){
            Institucion insti = buscaInsti(edittedEdu);       
            savedEdu.setInstitucion(insti);
        }
       savedEdu = this.eduServ.saveEducacion(savedEdu);
       return PasaADto.educacion(savedEdu);
       
    }
    
    private Institucion buscaInsti(EducacionDto eduDto){
        Institucion insti = this.instiServ.getInstitucionByNombre(eduDto.getNombreInstitucion());
            if(insti == null){
                insti = new Institucion();         
                insti.setNombre(eduDto.getNombreInstitucion());
                insti.setLogoUrl(eduDto.getFotoInstitucionUrl());
                insti = this.instiServ.saveInstitucion(insti);
            }else if (insti.getLogoUrl() == null){
                insti.setLogoUrl(eduDto.getFotoInstitucionUrl());
                insti = this.instiServ.saveInstitucion(insti);
            } 
        return insti;
            
            
    }
   
  
    
    }