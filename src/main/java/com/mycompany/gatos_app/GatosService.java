/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gatos_app;

import com.google.gson.Gson;
import java.awt.Image;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author serna
 */
public class GatosService {
    
    public static void verGatos() throws IOException{
        //1. Vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient();
        
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();
        
        Response response = client.newCall(request).execute();
        
        String elJson = response.body().string();
        
        //cortar los corchetes
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0 , elJson.length()-1);
        
        // Crear un objeto de la clase GSON
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJson, Gatos.class);
        
        // redimensionar en caso de necesitar
        Image image = null;
        
        try{
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);
            
            ImageIcon fondoGato = new ImageIcon(image);
            
            if(fondoGato.getIconWidth() > 800){
                //Redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }
            
            String menu = "Opciones: \n"
                    + "1. Ver Otra Imagen\n"
                    + "2. Favorito\n"
                    + "3. Volver \n";
            
            String[] botones = {"Ver Otra Imagen" , "Favorito" , "Volver"};
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato,JOptionPane.INFORMATION_MESSAGE , fondoGato , botones , botones[0]);
            
            int seleccion = -1;
            //Validamos que opcion selecciona el usuario
            for(int i=0; i<botones.length; i++){
                if(opcion.equals(botones[i])){
                    seleccion = i;
                }
            }
            
            switch (seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }
            
            
        }catch(IOException e){
            System.out.println("El error es" + e);
        }
        
    }
    
    public static void favoritoGato(Gatos gato){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
              .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\":\""+gato.getId()+"\"\r\n}");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites")
              .method("POST", body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gato.getApikey())
              .build();
            Response response = client.newCall(request).execute();
        }catch(IOException e){
            System.out.println("El error es " + e);
        }
    }
    
    public static void verFavorito(String apikey) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
          .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/favourites")
          .method("GET", null)
          .addHeader("Content-Type", "application/json")
          .addHeader("x-api-key", apikey)
          .build();
        Response response = client.newCall(request).execute();
        
        // Guardamos el string con la respuesta
        String elJson = response.body().string();
        
        // creamos el objeto gson
        
        Gson gson = new Gson();
        
        GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class);
        
        if(gatosArray.length > 0){
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max - min) + 1)) + min;
            int indice = aleatorio - 1;
            
            GatosFav gatofav = gatosArray[indice];
            
            // redimensionar en caso de necesitar
            Image image = null;

            try{
                URL url = new URL(gatofav.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon fondoGato = new ImageIcon(image);

                if(fondoGato.getIconWidth() > 800){
                    //Redimensionamos
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }

                String menu = "Opciones: \n"
                        + "1. Ver Otra Imagen\n"
                        + "2. Elimar Favorito\n"
                        + "3. Volver \n";

                String[] botones = {"Ver Otra Imagen" , "Eliminar Favorito" , "Volver"};
                String id_gato = gatofav.getId();
                String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato,JOptionPane.INFORMATION_MESSAGE , fondoGato , botones , botones[0]);

                int seleccion = -1;
                //Validamos que opcion selecciona el usuario
                for(int i=0; i<botones.length; i++){
                    if(opcion.equals(botones[i])){
                        seleccion = i;
                    }
                }

                switch (seleccion){
                    case 0:
                        verFavorito(apikey);
                        break;
                    case 1:
                        borrarFavorito(gatofav);
                        break;
                    default:
                        break;
                }


            }catch(IOException e){
                System.out.println("El error es" + e);
            }
        }
    }
    
    public static void borrarFavorito(GatosFav gatofav) throws IOException{
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
              .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites/"+gatofav.getId()+"")
              .method("DELETE", null)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gatofav.getApikey())
              .build();
            Response response = client.newCall(request).execute();       
        }catch(IOException e){
            System.out.println("El error es " + e);
        }
    }
}
