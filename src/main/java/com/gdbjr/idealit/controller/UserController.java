package com.gdbjr.idealit.controller;

import com.gdbjr.idealit.model.Empresa;
import com.gdbjr.idealit.model.Role;
import com.gdbjr.idealit.model.User;
import com.gdbjr.idealit.repository.EmpresaRepository;
import com.gdbjr.idealit.repository.RoleRepository;
import com.gdbjr.idealit.repository.UserRepository;
import com.gdbjr.idealit.security.TokenAuthenticationService;
import com.gdbjr.idealit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    UserService userService;

    @RequestMapping(value = { "/", ""} , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userRepository.findAllByEnabled("S");
        List<HashMap<String,Object>> lst = new ArrayList();

        users.forEach(row -> {
            int i = users.indexOf(row);
            users.get(i).setPassword(" [[[[ HIDDEN ]]]] "); // Hiding password
            HashMap<String,Object> m = new HashMap<>();
            m.put("id", row.getId());
            m.put("nome", row.getName());
            m.put("email", row.getMail());
            m.put("tipo", row.getRoles().iterator().next().getName());
            m.put("key", row.getKey());
            if (row.getEmpresa() != null)
                m.put("empresa", row.getEmpresa().getFantasia());
            lst.add(m);
        });
        // return new ResponseEntity(users, HttpStatus.OK);
        return new ResponseEntity(lst, HttpStatus.OK);

    }

    @RequestMapping(value = { "/{id}"} , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<User>> getUser(@PathVariable("id") Long id) {

        User user = userRepository.findByIdAndEnabled(id, "S");

        user.setPassword(" [[[[ HIDDEN ]]]] "); // Hiding password
        HashMap<String,Object> m = new HashMap<>();
        m.put("id", user.getId());
        m.put("nome", user.getName());
        m.put("email", user.getMail());
        m.put("tipo", user.getRoles().iterator().next().getName());
        if (user.getEmpresa() != null)
            m.put("empresa", user.getEmpresa().getFantasia());

        return new ResponseEntity(m, HttpStatus.OK);

    }

    @RequestMapping(value = { "/", ""} , method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<User>> setUser(@RequestHeader Map<String,Object> header, @RequestBody Map<String,Object> body) {

        String nome = body.get("nome").toString().trim();
        String email = body.get("email").toString().trim();
        String tipo = body.get("tipo").toString().trim();

        if ( !tipo.equals("ADMIN") && !tipo.equals("SYSTEM") && !tipo.equals("POPULACAO") ) {
            return new ResponseEntity("Error: tipo inválido [ Tipos: 'POPULACAO', 'SISTEMA' e 'ADMIN' ]", HttpStatus.OK);
        }

        Role role = roleRepository.findByName(tipo);
        Collection<Role> roles = new ArrayList();
        roles.add(role);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setName(nome);
        user.setMail(email);
        user.setKey( TokenAuthenticationService.keyGen(email) );
        user.setRoles(roles);
        if (body.get("empresa") != null) {
            Empresa empresa = empresaRepository.findByCnpjAndEnabled((String) body.get("empresa"), "S");
            if (empresa != null)
                user.setEmpresa(empresa);
            else
                return new ResponseEntity("Error: Empresa " + (String) body.get("empresa") + " não cadastrada.", HttpStatus.OK);
        }
        user.setPassword( encoder.encode( com.gdbjr.idealit.enums.RoleEnum.valueOf(tipo).getPassword() ) );
        try {
            userRepository.save(user);
            return new ResponseEntity("Success",  HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error", HttpStatus.OK);
        }
    }

    @RequestMapping(value = { "/{id}", ""} , method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<List<User>> updateUser(@PathVariable("id") Long id, @RequestHeader Map<String,Object> header, @RequestBody Map<String,Object> body) {

        if( id == null )
            return new ResponseEntity("Error: 'id' de usuário não informado.", HttpStatus.OK);

        User user = userRepository.findByIdAndEnabled(id, "S");
        if (user == null)
            return new ResponseEntity("Error: Uusário com ID = " + id + " não existente.", HttpStatus.OK);

        if (body.get("nome") != null)
            user.setName(body.get("nome").toString().trim());
        if (body.get("email") != null)
            user.setMail(body.get("email").toString().trim());
        if (body.get("nome") != null)
            user.setName(body.get("nome").toString().trim());
        if (body.get("empresa") != null) {
            Empresa empresa = empresaRepository.findByCnpjAndEnabled((String) body.get("empresa"), "S");
            if (empresa != null)
                user.setEmpresa(empresa);
            else
                return new ResponseEntity("Error: Empresa " + (String) body.get("empresa") + " não cadastrada.", HttpStatus.OK);
        }
        try {
            userRepository.save(user);
            return new ResponseEntity("Success",  HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error", HttpStatus.OK);
        }

    }

    @RequestMapping(value = { "/{id}", ""} , method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<List<User>> deleteUser(@PathVariable("id") Long id) {

        if( id == null )
            return new ResponseEntity("Error: 'id' de usuário não informado.", HttpStatus.OK);
        User user = null;
        if ( userRepository.findById(id).isPresent() )
            user = userRepository.findById(id).get();
        else
            return new ResponseEntity("Error: Uusário de " + id + " não existente.", HttpStatus.OK);

        user.setEnabled("N");

        try {
            userRepository.save(user);
            return new ResponseEntity("Success",  HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error", HttpStatus.OK);
        }
    }

}
