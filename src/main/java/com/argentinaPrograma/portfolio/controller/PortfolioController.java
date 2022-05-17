
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.argentinaPrograma.portfolio.controller;

import com.argentinaPrograma.portfolio.dto.EducacionByTipoDto;
import com.argentinaPrograma.portfolio.dto.EducacionDto;
import com.argentinaPrograma.portfolio.dto.ExperienciaDto;
import com.argentinaPrograma.portfolio.dto.PortfolioDto;
import com.argentinaPrograma.portfolio.model.Educacion;
import com.argentinaPrograma.portfolio.model.Experiencia;
import com.argentinaPrograma.portfolio.model.Perfil;
import com.argentinaPrograma.portfolio.model.TipoEducacion;
import com.argentinaPrograma.portfolio.service.IEducacionService;
import com.argentinaPrograma.portfolio.service.IPerfilService;
import com.argentinaPrograma.portfolio.service.ITipoEducacionService;
import com.argentinaPrograma.portfolio.service.PasaADto;
import static com.argentinaPrograma.portfolio.service.PasaADto.experiencia;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nahux
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    private IPerfilService perfilServ;
    
    @Autowired
    private ITipoEducacionService tipoEduServ;
    
    @Autowired 
    private IEducacionService eduServ;
    
    @GetMapping("/traer/{id_perfil}")
    @ResponseBody
    public PortfolioDto getPortfolio(@PathVariable Long id_perfil){
        
        PortfolioDto portfolio = new PortfolioDto();
        //Asigno Perfil
        Perfil perfil = this.perfilServ.getPerfilById(id_perfil);
        portfolio.setPerfil(PasaADto.perfil(perfil));        
        /*
        Asigno listado de educaciones by tipo
        */
        List<EducacionByTipoDto> educacionesByTipoDto = new ArrayList<>();
        List<TipoEducacion> tiposEdu = this.tipoEduServ.getTipoEstudios();
        for(TipoEducacion tipo:tiposEdu){
            EducacionByTipoDto eduByTipoDto = new EducacionByTipoDto();
            eduByTipoDto.setTipoEdu(tipo);
                        
            List<Educacion> educacionesByTipo = this.eduServ.getEducacionByPerfAndTipo(id_perfil, tipo.getId());
            List<EducacionDto> edusDtoByTipo = new ArrayList<>();
            for(Educacion edu: educacionesByTipo){
                edusDtoByTipo.add(PasaADto.educacion(edu));
            }
            eduByTipoDto.setEducaciones(edusDtoByTipo);
            educacionesByTipoDto.add(eduByTipoDto);
        }
        portfolio.setEducacionesByTipo(educacionesByTipoDto);      
        /*
        Agrego Experiencias        
        */
        List<ExperienciaDto> experiencias = new ArrayList<>();
        for(Experiencia exp:perfil.getExperiencias()){
            experiencias.add(experiencia(exp));
        }
        portfolio.setExperiencias(experiencias);
        return portfolio;
    } 
}
