/*
 * Autor: Tomeu Estrany Company
 * Asignatura: Programación Concurrente (UIB)
 * Curso 2021-2022
 */
package MixedBathroom;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TOMEU
 */
public class Home implements Runnable{
    
    private final int MAX_ENTRADES = 2;
    private String id;
    private String color;
    private Random rand;
    private final long MAX_WAIT;
    

    public Home(String id, String color) {
        this.id = id;
        this.color = color;
        this.rand = new Random();
        this.MAX_WAIT = 1000*rand.nextInt(3);
    }

    @Override
    public void run() {
        
        try {

            System.out.println(color+id+" ha entrado en el despacho");
            
            for (int i=1; i <= MAX_ENTRADES; i++){
                Banys.pref.acquire();
                Banys.mutex_homes.acquire();
                Banys.n_homes++;
                if (Banys.n_homes == 1){ 
                   Banys.bathroom.acquire();  //Si soy el primer hombre/mujer en entrar ocupo la SC
                }
                Banys.mutex_homes.release();
                Banys.pref.release();
                
                Banys.sem_homes.acquire();
                /*****INICIO SECCIÓN CRÍTICA*****/
                Banys.mutex_homes.acquire();
                Banys.persbany++;
                Banys.mutex_homes.release();
                
                Banys.mutex_homes.acquire();
                System.out.println(color+id+" entra "+i+"/"+MAX_ENTRADES+". Hombres en el baño "+Banys.persbany);
                Thread.sleep(MAX_WAIT);
                Banys.mutex_homes.release();               
                Thread.sleep(MAX_WAIT);
                Banys.mutex_homes.acquire();
                Banys.persbany--;
                Banys.mutex_homes.release();
                
                Banys.mutex_homes.acquire();
                System.out.println(color+id+ " sale");
                Thread.sleep(MAX_WAIT);
                Banys.mutex_homes.release();
                /*****FIN SECCIÓN CRÍTICA*****/
                Banys.sem_homes.release();
                
                Banys.mutex_homes.acquire();
                Banys.n_homes--;
                
                if (Banys.n_homes == 0){
                    System.out.println("*** El baño está vacío***");
                    Banys.bathroom.release();
                }
                Banys.mutex_homes.release();
 
   
            }
            
            System.out.println(color+id+" acaba el trabajo");
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
}
