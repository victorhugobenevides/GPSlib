
package br.com.gps.onnet.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import br.com.gps.onnet.obj.Posicao;


import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;


/**
 * Biblioteca de requisi��o de posi��es de GPS e geofence
 * @author Victor Hugo Benevides Sobrinho
 * victorhugobenevides@hotmail.com
 * 
 * 
 * 
 * 
 * */


public class GPS extends Thread implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,LocationListener, LocationClient.OnAddGeofencesResultListener, LocationClient.OnRemoveGeofencesResultListener {
	
	

	
	private LocationClient locationclient;
	private LocationRequest locationrequest;
	private Intent mIntentService;
	private PendingIntent mPendingIntent;
	private int Status=0;
	
	
	
	private static int maxqualidadedistancia=60;
	private static long IntervaloAtualizacao=1000;
	private static int Prioridade=4;
/**	Id da Broadcast que indica que o cliente esta no raio de proximidade*/
	public static final String ENTROUNOCLIENTE="br.com.gps.onnet.ENTROUNOCLIENTE";
/**	Id da Broadcast que indica que o cliente saiu do raio de proximidade*/
	public static final String SAIUDOCLIENTE="br.com.gps.onnet.SAIUDOCLIENTE";
/**	Prioridade da qualidade posi��es gps = pega melhores posicoes (wifi,rede e gps), gasta mais bateria*/
	public static final int PRIORIDADEMAX=1;
/**	Prioridade da qualidade posi��es gps = pega posicoes balanceadas e economiza bateria*/
	public static final int PRIORIDADEBALANCEADA=2;
/**	Prioridade da qualidade posi��es gps = pega posicoes com baixa qualidade e economiza bateria*/
	public static final int PRIORIDADEBAIXA=3;
/**	Prioridade da qualidade posi��es gps = pega piores posicoes e economiza bateria*/
	public static final int PRIORIDADEMUITOBAIXA=4;
	

	private Context ContextApp;
	
	private  double Latitude;
	private  double Longitude;
	private  long Data;
	private  float Velocidade;
	private  float Qualidade;
	
	private  double LatitudeMelhor;
	private  double LongitudeMelhor;
	private  long DataMelhor;
	private  float VelocidadeMelhor;
	private  float QualidadeMelhor;
	
	
	/**Construtor recebe o contexto da aplica��o*/
	 public GPS(Context context) {
		// TODO Auto-generated constructor stub
		 ContextApp=context;
	}
/**	 Remove todos os clientes adicionados no geofence*/
	public void RemoveTodosGeofences() throws Exception{
		locationclient.removeGeofences(mPendingIntent, this);
		locationclient.removeGeofences(mPendingIntent, this);
	}
	/**	 Remove o cliente com o id adicionado no geofence*/
	public void RemoveGeofence(String id) throws Exception{
		List<String> geofList = new ArrayList<String>();
		geofList.add(id);
		locationclient.removeGeofences(geofList,this);
	}
	
	/**	Remove geofences da lista de ids dos clientes*/
	public void RemoveGeofences(List<String> geofList) throws Exception{
		
		locationclient.removeGeofences(geofList,this);
		
	}
	
/**	Adiciona cliente para verificar o raio de proximidade*/
	public void AddGeofences(String codigo, long lat,long lon,float distancia) throws Exception{
 
		  locationclient.removeGeofences(mPendingIntent, this);

		  Geofence geofence = new Geofence.Builder()
          .setRequestId(codigo)
          .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
          .setCircularRegion(lat,lon,distancia)
          .setExpirationDuration(Geofence.NEVER_EXPIRE).build();
		  
		  List<Geofence> geofences = new ArrayList<Geofence>();
		  geofences.add(geofence);
		  locationclient.addGeofences(geofences, mPendingIntent, this);
			
	    }
	
@Override
public void run() {
	// TODO Auto-generated method stub
	try {
		Iniciar();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	super.run();
}

/**Inicia o recebimento de posi��es GPS*/
	private void Iniciar()  throws Exception{



		
	
		
		mIntentService = new Intent(ContextApp,ReceiveTransitionsIntentService.class);
		mPendingIntent = PendingIntent.getService(ContextApp,0,mIntentService,PendingIntent.FLAG_UPDATE_CURRENT);
		locationclient = new LocationClient(ContextApp,this,this);
		locationclient.connect();

	}
	
	
	/**	Finaliza o recebimento de posi��es GPS*/
	public void Desconectar(){
		
		try {
			locationclient.disconnect();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		setStatus(0);
	}
	
	/**	inicia o recebimento de posi��es*/
	public void Conectar(){
		this.run();
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		try {

			if (location != null) {

				
				setStatus(2);
				
				
					setLatitude(location.getLatitude());
					setLongitude(location.getLongitude());
					setData(location.getTime());
					setVelocidade(location.getSpeed());
					setQualidade(location.getAccuracy());
					
					
					
					
				
				
			
				
				
				
				
				
				
				try {
					
					
					
					
						
						
					setLatitudeMelhor(location.getLatitude());
					setLongitudeMelhor(location.getLongitude());
					setDataMelhor(location.getTime());
					setVelocidadeMelhor(location.getSpeed());
					setQualidadeMelhor(location.getAccuracy());
						
			
							
							
				
				
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}	
		  
		
		
	}
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

		
		
		mIntentService = new Intent(ContextApp,ReceiveTransitionsIntentService.class);
		mPendingIntent = PendingIntent.getService(ContextApp,0,mIntentService,PendingIntent.FLAG_UPDATE_CURRENT);
		locationclient = new LocationClient(ContextApp,this,this);
		locationclient.connect();
		
	}
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
		try {
			
			
			
			setStatus(1);
			locationrequest = LocationRequest.create();
		     locationrequest.setInterval(IntervaloAtualizacao);
		     locationrequest.setFastestInterval(IntervaloAtualizacao/6);
		     if(Prioridade==4){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
		     }else if(Prioridade==3){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		     }else if(Prioridade==2){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		     }else if(Prioridade==1){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		     }else{
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);	
		     }

		     locationclient.requestLocationUpdates(locationrequest, this);

		     
		     
		     
		} catch (Exception e) {
			// TODO: handle exception
			int i=0;
			i++;
		}
		
		
	}
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

		
		
		mIntentService = new Intent(ContextApp,ReceiveTransitionsIntentService.class);
		mPendingIntent = PendingIntent.getService(ContextApp,0,mIntentService,PendingIntent.FLAG_UPDATE_CURRENT);
		locationclient = new LocationClient(ContextApp,this,this);
		locationclient.connect();
	}
	
	
	

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// TODO Auto-generated method stub
		if(LocationStatusCodes.SUCCESS == statusCode){
			int i=0;
			i++;
		}
		
	}


	@Override
	public void onRemoveGeofencesByPendingIntentResult(int arg0,
			PendingIntent arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRemoveGeofencesByRequestIdsResult(int arg0, String[] arg1) {
		// TODO Auto-generated method stub
		
	}

	/**Recebe qual � a distancia de qualidades em metros do filtro das posi��es definida (usar os dados com "MELHOR" no final do get)*/
	public static int getMaxqualidadedistancia() {
		return maxqualidadedistancia;
	}

	/**Define qual distancia para filtro de qualidades em metros das posi��es (usar os dados com "MELHOR" no final do get)*/
	public static void setMaxqualidadedistancia(int maxqualidadedistancia) {
		GPS.maxqualidadedistancia = maxqualidadedistancia;
	}
/**	Retorna latitude sem levar em considera��o o filtro de qualidade definido*/
	public double getLatitude() {
		return Latitude;
	}
	private void setLatitude(double latitude) {
		Latitude = latitude;
	}
	/**	Retorna Longitude sem levar em considera��o o filtro de qualidade definido*/
	public double getLongitude() {
		return Longitude;
	}
	private void setLongitude(double longitude) {
		Longitude = longitude;
	}
	/**	Retorna data sem levar em considera��o o filtro de qualidade definido*/
	public long getData() {
		return Data;
	}
	private void setData(long data) {
		Data = data;
	}
	/**	Retorna velocidade sem levar em considera��o o filtro de qualidade definido*/
	public float getVelocidade() {
		return Velocidade;
	}
	private void setVelocidade(float velocidade) {
		Velocidade = velocidade;
	}
	/**	Retorna Qualidade sem levar em considera��o o filtro de qualidade definido*/
	public float getQualidade() {
		return Qualidade;
	}
	private void setQualidade(float qualidade) {
		Qualidade = qualidade;
	}
	/**Retorna latitude de acordo com o filtro de qualidade setado*/
	public double getLatitudeMelhor() {
		return LatitudeMelhor;
	}
	private void setLatitudeMelhor(double latitudeMelhor) {
		LatitudeMelhor = latitudeMelhor;
	}
	/**Retorna longitude de acordo com o filtro de qualidade setado*/
	public double getLongitudeMelhor() {
		return LongitudeMelhor;
	}
	private void setLongitudeMelhor(double longitudeMelhor) {
		LongitudeMelhor = longitudeMelhor;
	}
	/**Retorna data de acordo com o filtro de qualidade setado*/
	public long getDataMelhor() {
		return DataMelhor;
	}
	private void setDataMelhor(long dataMelhor) {
		DataMelhor = dataMelhor;
	}
	/**Retorna velocidade de acordo com o filtro de qualidade setado*/
	public float getVelocidadeMelhor() {
		return VelocidadeMelhor;
	}
	private void setVelocidadeMelhor(float velocidadeMelhor) {
		VelocidadeMelhor = velocidadeMelhor;
	}
	/**Retorna qualidade de acordo com o filtro de qualidade setado*/
	public float getQualidadeMelhor() {
		return QualidadeMelhor;
	}
	private void setQualidadeMelhor(float qualidadeMelhor) {
		QualidadeMelhor = qualidadeMelhor;
	}
	/**Retorna status do sistema: 
	 * <br>0 = N�o conectado.
	 * <br>1 = Conectado sem pegar posi��o
	 * <br>2 = Conectado pegando posi��es
	 * */
	public int getStatus() {
		
		
		if(!locationclient.isConnected()){
			return 0;
		}
		
		return Status;
	}
	private void setStatus(int status) {
		Status = status;
	}
/**Retorna o intervalo de atualiza��o de posi��o em millisegundos*/
	public static long getIntervaloAtualizacao() {
		return IntervaloAtualizacao;
	}
	/**Seta o intervalo de atualiza��o de posi��o em millisegundos*/
	public static void setIntervaloAtualizacao(long intervaloAtualizacao) {
		IntervaloAtualizacao = intervaloAtualizacao;
	}
	/**Retorna prioridade do GPS:
	 * 
	 *<br> PRIORIDADEMAX=1
	 * <br>Usa todos os meios de localiza��o, mas prioriza o GPS. 10 metros de alcance
	 * <br>PRIORIDADEBALANCEADA=2
	 * <br>Exclui o GPS da lista de provedores de posi��o, usa torre de celular, wifi e etc. 40 metros
	 * <br>PRIORIDADEBAIXA=3
	 * <br>Qualidade muito ruim, 10km de precis�o
	 * <br>PRIORIDADEMUITOBAIXA=4
	 * <br> N�o usa sistema de localiza��o.
	 * */
	public static int getPrioridade() {
		return Prioridade;
	}
	
	
	/**Seta prioridade do GPS(Usar flags pr� definidas):
	 * 
	 *<br> PRIORIDADEMAX=1
	 * <br>Usa todos os meios de localiza��o, mas prioriza o GPS. 10 metros de alcance
	 * <br>PRIORIDADEBALANCEADA=2
	 * <br>Exclui o GPS da lista de provedores de posi��o, usa torre de celular, wifi e etc. 40 metros
	 * <br>PRIORIDADEBAIXA=3
	 * <br>Qualidade muito ruim, 10km de precis�o
	 * <br>PRIORIDADEMUITOBAIXA=4
	 * <br> N�o usa sistema de localiza��o.
	 * */
	public static void setPrioridade(int prioridade) {
		Prioridade = prioridade;
	}
	
	


}
