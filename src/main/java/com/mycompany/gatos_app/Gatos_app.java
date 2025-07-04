/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gatos_app;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author serna
 */
public class Gatos_app {

    public static void main(String[] args) throws IOException {
        int opcion_menu = -1;
        String[] botones = {"1. Ver Gatos" , "2. Ver favoritos" ,"3. Salir"};
        
        do{
            String opcion =(String) JOptionPane.showInputDialog(null , "Gatitos Java" , "Menu principal" , JOptionPane.INFORMATION_MESSAGE ,
                    null , botones, botones[0]);
            
            //Validamos que opcion selecciona el usuario
            for(int i = 0; i < botones.length ; i++){
                if(opcion.equals(botones[i])){
                opcion_menu = i;
                }
            }
            
            switch(opcion_menu){
                case 0:
                    GatosService.verGatos();
                    break;
                case 1:
                    Gatos gato = new Gatos();
                    GatosService.verFavorito(gato.getApikey());
                    break;
                default:
                    break;       
            }
            
        }while(opcion_menu != 1);
        
    }
}
