/*
 * Autor: Tomeu Estrany Company
 * Asignatura: Programación Concurrente (UIB)
 * Curso 2021-2022
 */
package MixedBathroom;

import java.util.concurrent.Semaphore;

/**
 *
 * @author TOMEU
 */
public class Banys {
    
    static final int CAPACITAT = 3; //Numero máximo de procesos en la sección crítica
    static final int HOMES = 6; //Numero máximo de hombres y mujeres
    static final int DONES = 6;
    static final String[] NOMHOMES = {"Jaume", "Toni", "Rafel", "Pep", "Miquel", "Biel"};
    static final String[] NOMDONES = {"Marga", "Joana", "Maria", "Carla", "Mireia", "Aina"};
    static final String[] COLORS = {"\u001B[31m","\u001B[33m","\u001B[34m",
    "\u001B[35m","\u001B[36m","\u001B[32m"} ;
    
    public static Semaphore sem_homes = new Semaphore(CAPACITAT); //control sección crítica baño
    public static Semaphore sem_dones = new Semaphore(CAPACITAT);
    
    public static Semaphore mutex_homes = new Semaphore(1); //Protege incrementos y mensages de salida
    public static Semaphore mutex_dones = new Semaphore(1); 
    
    public static Semaphore bathroom = new Semaphore(1); //ocupación del baño
    public static Semaphore pref = new Semaphore(1); //preferencia de sexo (evita inanición)
    
    static int persbany = 0;    //personas en la SC
    static int n_homes = 0;     //personas que requieren el baño
    static int n_dones = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread[] homes = new Thread[HOMES];
        Thread[] dones = new Thread[DONES];
        int i;
        
        for (i=0; i < HOMES; i++){
            homes[i] = new Thread(new Home(NOMHOMES[i], COLORS[i]));
            homes[i].start();
        }
        for (i=0; i < DONES; i++){
            dones[i] = new Thread(new Dona(NOMDONES[i], COLORS[i]));
            dones[i].start();
        }
        
        for (i=0; i < HOMES; i++){
            homes[i].join();
        }
        
        for (i=0; i < DONES; i++){
            dones[i].join();
        }
    }
    
}
