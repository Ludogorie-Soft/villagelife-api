package com.example.ludogoriesoft.village.authorization;

import com.example.ludogoriesoft.village.services.AdministratorService;
import com.ludogorieSoft.villagelifefrontend.config.AdminClient;
import com.ludogorieSoft.villagelifefrontend.dtos.AdministratorDTO;
import com.ludogorieSoft.villagelifefrontend.dtos.AdministratorRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {
   private AdministratorService administratorService;
    private ModelMapper modelMapper;
    public AdministratorRequest administratorRequestToAdministratorDTO(AdministratorDTO administratorDTO) {
        return modelMapper.map(administratorDTO, AdministratorRequest.class);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdministratorDTO administratorDTO = administratorService.(username);
        if(administratorDTO == null){
            throw new UsernameNotFoundException("Administrator not found");
        }
       AdministratorRequest administratorRequest =  administratorRequestToAdministratorDTO(administratorDTO);
        return new MyUserDetails(administratorRequest);
    }
}
