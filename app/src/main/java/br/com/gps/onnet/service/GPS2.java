
package br.com.gps.onnet.service;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Biblioteca de requisi��o de posi��es de GPS e geofence
 * @author Victor Hugo Benevides Sobrinho
 * victorhugobenevides@hotmail.com
 * 
 * 
 * 
 * 
 * */


public class GPS2   implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {




	private GoogleApiClient mGoogleApiClient;
	private LocationRequest locationrequest;
	private Intent mIntentService;
	private PendingIntent mPendingIntent;
	private LocationListener locationListener;
	private int Status=0;



	private static int maxqualidadedistancia=60;
	private static long distanciaLocation=10;
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





	/**Construtor recebe o contexto da aplica��o*/
	 public GPS2(Context context) {
		// TODO Auto-generated constructor stub
		 ContextApp=context;
	}









/**Inicia o recebimento de posi��es GPS*/
protected synchronized void  Iniciar()  throws Exception{



		mGoogleApiClient = new GoogleApiClient.Builder(ContextApp)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}



	}


	/**	Finaliza o recebimento de posi��es GPS*/
	public void Desconectar(){

		try {
			if(locationListener!=null){
				LocationServices.FusedLocationApi.removeLocationUpdates(
						mGoogleApiClient, locationListener);
			}else{
				//LocationServices.FusedLocationApi.removeLocationUpdates(
				//		mGoogleApiClient, this);

			}


		} catch (Exception e) {
			// TODO: handle exception
		}
		setStatus(0);
	}

	/**	inicia o recebimento de posi��es*/
	public void Conectar(){

        try {
            Iniciar();
        }catch (Exception e){

        }


	}


	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub



		mGoogleApiClient = new GoogleApiClient.Builder(ContextApp)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}

	}
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

		try {



			setStatus(1);
			locationrequest = LocationRequest.create();
		     locationrequest.setInterval(IntervaloAtualizacao);
		     locationrequest.setFastestInterval(IntervaloAtualizacao/6);
		     if(Prioridade==PRIORIDADEMUITOBAIXA){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
		     }else if(Prioridade==PRIORIDADEBAIXA){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		     }else if(Prioridade==PRIORIDADEBALANCEADA){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		     }else if(Prioridade==PRIORIDADEMAX){
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		     }else{
		    	 locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		     }
			locationrequest.setSmallestDisplacement(distanciaLocation);

			if(locationListener!=null){
				LocationServices.FusedLocationApi.requestLocationUpdates(
						mGoogleApiClient, locationrequest, locationListener);
			}


		     
		     
		     
		} catch (Exception e) {
			// TODO: handle exception
			int i=0;
			i++;
		}
		
		
	}

	@Override
	public void onConnectionSuspended(int i) {

		if(mGoogleApiClient!=null)
		mGoogleApiClient.connect();

	}

	/**intervalo de solicitar localização por distancia*/
	public static long getDistanciaLocation() {
		return distanciaLocation;
	}
	/**intervalo de solicitar localização por distancia*/
	public static void setDistanciaLocation(long distanciaLocation) {
		GPS2.distanciaLocation = distanciaLocation;
	}

	/**Recebe qual � a distancia de qualidades em metros do filtro das posi��es definida (usar os dados com "MELHOR" no final do get)*/
	public static int getMaxqualidadedistancia() {
		return maxqualidadedistancia;
	}

	/**Define qual distancia para filtro de qualidades em metros das posi��es (usar os dados com "MELHOR" no final do get)*/
	public static void setMaxqualidadedistancia(int maxqualidadedistancia) {
		GPS2.maxqualidadedistancia = maxqualidadedistancia;
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

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    /**Retorna status do sistema:
	 * <br>0 = N�o conectado.
	 * <br>1 = Conectado sem pegar posi��o
	 * <br>2 = Conectado pegando posi��es
	 * */
	public int getStatus() {
		
		
		if(!mGoogleApiClient.isConnected()){
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





	public Location getLocation() {

		Location mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			setStatus(2);
			setLatitude(mLastLocation.getLatitude());
			setLongitude(mLastLocation.getLongitude());
			setData(mLastLocation.getTime());
			setVelocidade(mLastLocation.getSpeed());
			setQualidade(mLastLocation.getAccuracy());



		}
        return mLastLocation;


	}


}
