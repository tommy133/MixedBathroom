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
public class Dona implements Runnable{
    private final int MAX_ENTRADES = 2;
    private String id;
    private String color;    
    private Random rand;
    private final long MAX_WAIT;

    public Dona(String id, String color) {
        this.id = id;
        this.color = color;
        this.rand = new Random();
        this.MAX_WAIT = 1000*rand.nextInt(3);
    }

    @Override
    public void run() {
        try {
            System.out.println("\t"+color+id+" ha entrado en el despacho");
            for (int i=1; i <= MAX_ENTRADES; i++){
                Banys.pref.acquire();
                Banys.mutex_dones.acquire();
                Banys.n_dones++;
                if (Banys.n_dones == 1){
                    Banys.bathroom.acquire();  
                }
                Banys.mutex_dones.release();
                Banys.pref.release();
                
                Banys.sem_dones.acquire();
                /*****INICIO SECCIÓN CRÍTICA*****/
                Banys.mutex_dones.acquire();
                Banys.persbany++;
                Banys.mutex_dones.release();
                
                Banys.mutex_dones.acquire();
                System.out.println("\t"+color+id+" entra "+i+"/"+MAX_ENTRADES+". Mujeres en el baño "+Banys.persbany);
                Thread.sleep(MAX_WAIT);
                Banys.mutex_dones.release();               
                Thread.sleep(MAX_WAIT);
                Banys.mutex_dones.acquire();
                Banys.persbany--;
                Banys.mutex_dones.release();
                
                Banys.mutex_dones.acquire();
                System.out.println("\t"+color+id+ " sale");
                Thread.sleep(MAX_WAIT);
                Banys.mutex_dones.release();
                /*****FIN SECCIÓN CRÍTICA*****/
                Banys.sem_dones.release();
                
                Banys.mutex_dones.acquire();
                Banys.n_dones--;

                if (Banys.n_dones == 0){
                    System.out.println("\t"+"*** El baño está vacío***");
                    Banys.bathroom.release();
                }
                Banys.mutex_dones.release();
                
            }
            System.out.println("\t"+color+id+" acaba el trabajo");
        } catch (InterruptedException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
