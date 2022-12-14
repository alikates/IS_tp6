package es.unizar.eina.appPedidos.send;

import android.app.Activity;

/** Implementa la interfaz de la abstraccion utilizando (delegando a) una referencia a un objeto de tipo implementor  */
public class SendAbstractionImpl implements SendAbstraction {
	
	/** objeto delegado que facilita la implementacion del metodo send */
	private SendImplementor implementor;
	
	/** Constructor de la clase. Inicializa el objeto delegado
	 * @param sourceActivity actividad desde la cual se abrira la actividad encargada de enviar la nota
	 * @param method parametro potencialmente utilizable para instanciar el objeto delegado
	 */
	public SendAbstractionImpl(Activity sourceActivity, String method) {
		switch (method) {
			case "SMS":
				implementor = new SMSImplementor(sourceActivity);
				break;
			case "WA":
				implementor = new WAImplementor(sourceActivity);
				break;
			case "MAIL":
				implementor = new MailImplementor(sourceActivity);
				break;
		}
	}

	/** Envia la correo con el asunto (subject) y cuerpo (body) que se reciben como parametros a traves de un objeto delegado
     * @param subject asunto
     */
	public void send(String subject) {
		implementor.send(subject);
	}
}
