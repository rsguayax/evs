package es.grammata.evaluation.evs.data.security;
 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
public class GenPass {
 
    public static void main(String args[]) throws Exception {
        String cryptedPassword = new BCryptPasswordEncoder().encode("test");
        System.out.println(cryptedPassword);
    }
}