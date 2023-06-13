package com.example.ludogoriesoft.village.authorization;//package com.ludogoriesoft.villagelifefrontend.authorization;
//
//
//import com.ludogoriesoft.villagelifefrontend.config.AdminClient;
//import com.ludogoriesoft.villagelifefrontend.dtos.AdministratorDTO;
//import com.ludogoriesoft.villagelifefrontend.dtos.AdministratorRequest;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//@AllArgsConstructor
//@NoArgsConstructor
//public class UserDetailsServiceImplementation implements UserDetailsService {
//   private AdminClient adminClient;
//   private ModelMapper modelMapper;
//
//    public AdministratorRequest administratorDTOToAdministratorRequest(AdministratorDTO administratorDTO){
//        return modelMapper.map(administratorDTO, AdministratorRequest.class);
//    }
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("1");
//
//        AdministratorRequest administratorRequest = adminClient.getAdministratorByUsername(username);
//
//        System.out.println("2");
//
//        if(administratorRequest == null){
//            throw new UsernameNotFoundException("Administrator not found");
//        }
//        System.out.println("3");
//
//        return new MyUserUserDetails(administratorRequest);//new MyUserDetails(administratorDTOToAdministratorRequest(administratorDTO));
//    }
//}
