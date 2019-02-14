package com.gdbjr.idealit.controller;


import com.gdbjr.idealit.model.Empresa;
import com.gdbjr.idealit.model.Incidente;
import com.gdbjr.idealit.repository.EmpresaRepository;
import com.gdbjr.idealit.repository.IncidenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    IncidenteRepository incidenteRepository;

    @RequestMapping(value = { "/", ""} , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Empresa>> getEmpresas() {

        List<Empresa> empresas = empresaRepository.findAllByEnabled("S");
        List<HashMap<String,Object>> lst = new ArrayList();

        empresas.forEach(row -> {
            HashMap<String,Object> m = new HashMap<>();
            m.put("cnpj", row.getCnpj());
            m.put("fantasia", row.getFantasia());
            m.put("contato", row.getContato());

            if (!row.getIncidentes().isEmpty()) {
                List<Incidente> incidenteLst = (List<Incidente>) row.getIncidentes();
                Incidente incidente = incidenteLst.get(incidenteLst.size() -1);
                m.put("nivelPerigo", incidente.getNivelPerigo());
                if ( incidente.getComentario() != null )
                    if ( !incidente.getComentario().equals("") )
                        m.put("comentario", incidente.getComentario());
                m.put("status", incidente.getStatus());
            }
            lst.add(m);
        });
        return new ResponseEntity(lst, HttpStatus.OK);

    }

    @RequestMapping(value = { "/{cnpj}"} , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Empresa>> getEmpresa(@PathVariable("cnpj") String cnpj) {

        Empresa empresa = empresaRepository.findByCnpjAndEnabled(cnpj, "S");

        HashMap<String,Object> m = new HashMap<>();
        m.put("cnpj", empresa.getCnpj());
        m.put("fantasia", empresa.getFantasia());
        m.put("contato", empresa.getContato());

        if (!empresa.getIncidentes().isEmpty()) {
            List<Incidente> incidenteLst = (List<Incidente>) empresa.getIncidentes();
            Incidente incidente = incidenteLst.get(incidenteLst.size() -1);
            m.put("nivelPerigo", incidente.getNivelPerigo());
            if ( incidente.getComentario() != null )
                if ( !incidente.getComentario().equals("") )
                    m.put("comentario", incidente.getComentario());
            m.put("status", incidente.getStatus());
        }
        return new ResponseEntity(m, HttpStatus.OK);

    }

    @RequestMapping(value = { "/", ""} , method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Empresa>> setEmpresa(@RequestHeader Map<String,Object> header, @RequestBody Map<String,Object> body) {

        String cnpj = body.get("cnpj").toString().trim();
        String fantasia = body.get("fantasia").toString().trim();
        String contato = body.get("contato").toString().trim();

        if (cnpj.length() != 14)
            return new ResponseEntity("Error: cnpj inválido", HttpStatus.OK);

        Empresa empresa = new Empresa();
        empresa.setCnpj(cnpj);
        empresa.setFantasia(fantasia);
        empresa.setContato(contato);

        try {
            empresaRepository.save(empresa);
            return new ResponseEntity("Success",  HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error", HttpStatus.OK);
        }

    }

    @RequestMapping(value = { "/{cnpj}"} , method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Empresa>> setEmpresa(@PathVariable("cnpj") String cnpj, @RequestHeader Map<String,Object> header, @RequestBody Map<String,Object> body) {

        if( cnpj == null )
            return new ResponseEntity("Error: 'cnpj' de usuário não informado.", HttpStatus.OK);
        if ( body.get("nivelPerigo") == null || body.get("data") == null || body.get("status") == null )
            return new ResponseEntity("Error: Os campos 'nivelPerigo', 'data' e 'status' são obrigatórios", HttpStatus.OK);
        Empresa empresa = empresaRepository.findByCnpjAndEnabled(cnpj, "S");
        if ( empresa == null )
            return new ResponseEntity("Error: Empresa com CNPJ = " + cnpj + " não existente.", HttpStatus.OK);

        String nivelPerigo = body.get("nivelPerigo").toString().trim();
        String status = body.get("status").toString().trim();
        String comentario = ( body.get("comentario") != null ) ? body.get("comentario").toString().trim() : null;
        String strDate = body.get("data").toString().trim().replaceAll("/", "-");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(strDate);
            Incidente incidente = new Incidente();
            incidente.setNivelPerigo(nivelPerigo);
            incidente.setStatus(status);
            incidente.setComentario(comentario);
            incidente.setData(date);
            Collection<Incidente> incidentes = empresa.getIncidentes();
            incidentes.add(incidente);
            empresa.setIncidentes(incidentes);
            try {
                empresaRepository.save(empresa);
                return new ResponseEntity("Success",  HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity("Error", HttpStatus.OK);
            }
        } catch (ParseException e) {
            return new ResponseEntity("Error: Campo date inválido", HttpStatus.OK);
        }

    }

    @RequestMapping(value = { "/{cnpj}", ""} , method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<List<Empresa>> updateEmpresa(@PathVariable("cnpj") String cnpj, @RequestHeader Map<String,Object> header, @RequestBody Map<String,Object> body)
            throws ParseException {

        if( cnpj == null )
            return new ResponseEntity("Error: 'cnpj' de usuário não informado.", HttpStatus.OK);
        if ( body.get("idAlerta") == null )
            return new ResponseEntity("Error: O campo 'idAlerta'é obrigatório", HttpStatus.OK);
        Empresa empresa = empresaRepository.findByCnpjAndEnabled(cnpj, "S");
        if ( empresa == null )
            return new ResponseEntity("Error: Empresa com CNPJ = " + cnpj + " não existente.", HttpStatus.OK);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Long idAlerta = Long.valueOf( body.get("idAlerta").toString() );
        String nivelPerigo = ( body.get("nivelPerigo") != null ) ? body.get("nivelPerigo").toString().trim() : null;
        String comentario = ( body.get("comentario") != null ) ? body.get("comentario").toString().trim() : null;
        Date data = ( body.get("data") != null ) ? formatter.parse( body.get("data").toString().trim().replaceAll("/", "-") ) : null;
        String status = ( body.get("status") != null ) ? body.get("status").toString().trim() : null;
        Date dataResolucao = ( body.get("dataResolucao") != null ) ? formatter.parse( body.get("dataResolucao").toString().trim().replaceAll("/", "-") ) : null;

        Collection<Incidente> incidentes = empresa.getIncidentes();
        boolean has = false;
        for (Incidente incidente: incidentes) {
            if (incidente.getId() == idAlerta) {
                has = true;
                if (nivelPerigo != null) incidente.setNivelPerigo(nivelPerigo);
                if (comentario != null) incidente.setComentario(comentario);
                if (data != null) incidente.setData(data);
                if (status != null) incidente.setStatus(status);
                if (dataResolucao != null) incidente.setDataResolucao(dataResolucao);
                incidenteRepository.save(incidente);
            }
        }
        if ( !has )
            return new ResponseEntity("O idAlerta: " + idAlerta.toString() + " não pertence a empresa de cnpj: " + cnpj, HttpStatus.OK);

        return new ResponseEntity("Success",  HttpStatus.OK);
    }


    @RequestMapping(value = { "/{cnpj}", ""} , method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<List<Empresa>> deleteEmpresa(@PathVariable("cnpj") String cnpj) {

        if( cnpj == null )
            return new ResponseEntity("Error: 'cnpj' de usuário não informado.", HttpStatus.OK);
        Empresa empresa = empresaRepository.findByCnpjAndEnabled(cnpj, "S");
        if ( empresa == null )
            return new ResponseEntity("Error: Empresa com CNPJ = " + cnpj + " não existente.", HttpStatus.OK);

        empresa.setEnabled("N");

        try {
            empresaRepository.save(empresa);
            return new ResponseEntity("Success",  HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error", HttpStatus.OK);
        }

    }




    // PRIVATE FUNCTIONS //

    private boolean isValidCnpj(String CNPJ) {
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return(false);

        char dig13, dig14;
        int sm, i, r, num, peso;
        try {
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
                num = (int)(CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char)((11-r) + 48);
            sm = 0;
            peso = 2;
            for (i=12; i>=0; i--) {
                num = (int)(CNPJ.charAt(i)- 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char)((11-r) + 48);
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }

}
