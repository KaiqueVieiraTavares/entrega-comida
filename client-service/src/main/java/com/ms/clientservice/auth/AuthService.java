package com.ms.clientservice.auth;

import com.ms.clientservice.entities.ClientEntity;
import com.ms.clientservice.exceptions.ClientNotFoundException;
import com.ms.clientservice.exceptions.EmailAlreadyExistsException;
import com.ms.clientservice.repositories.ClientRepository;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    public AuthService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseAuthDto registerUser(RegisterDto registerDto){
        if(clientRepository.existsByEmail(registerDto.email())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        var client = new ClientEntity();
        client.setEmail(registerDto.email());
        client.setNome(registerDto.name());
        client.setPassword(passwordEncoder.encode(registerDto.password()));
        var savedUser = clientRepository.save(client);
        var token = tokenService.generateToken(savedUser);
        return new ResponseAuthDto(savedUser.getId(), token, savedUser.getNome(), savedUser.getEmail());
    }
    public ResponseAuthDto loginUser(LoginDto loginDto){
        var auth = new UsernamePasswordAuthenticationToken(
                loginDto.email(),
                loginDto.password()
        );
        authenticationManager.authenticate(auth);
        var client = clientRepository.findByEmail(loginDto.email()).orElseThrow(()-> new ClientNotFoundException("Client not found"));
        var token = tokenService.generateToken(client);
        return new ResponseAuthDto(client.getId(), token, client.getNome(), client.getEmail());
    }
}
