package es.unizar.eina.send;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class WAImplementor implements SendImplementor {

    /** actividad desde la cual se abrir� la actividad de gesti�n de correo */
    private Activity sourceActivity;

    /** Constructor
     * @param source actividad desde la cual se abrir� la actividad de gesti�n de correo
     */
    public WAImplementor(Activity source){
        setSourceActivity(source);
    }

    /**  Actualiza la actividad desde la cual se abrir� la actividad de gesti�n de correo */
    public void setSourceActivity(Activity source) {
        sourceActivity = source;
    }

    /**  Recupera la actividad desde la cual se abrirá la actividad de gestión de correo */
    public Activity getSourceActivity(){
        return sourceActivity;
    }

    /**
     * Implementaci�n del m�todo send utilizando la aplicaci�n de gesti�n de correo de Android
     * Solo se copia el asunto y el cuerpo
     * @param subject asunto
     */
    public void send (String subject) {
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT , subject);
        intent.setPackage("com.whatsapp");
        getSourceActivity().startActivity(intent);
    }
}
