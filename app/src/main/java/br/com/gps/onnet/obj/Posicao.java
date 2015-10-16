package br.com.gps.onnet.obj;

import java.util.Calendar;

public class Posicao {
	private static double Latitude;
	private static double Longitude;
	private static long Data;
	private static float Velocidade;
	private static float Qualidade;
	
	public static double getLatitude() {
		return Latitude;
	}
	public static void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public static double getLongitude() {
		return Longitude;
	}
	public static void setLongitude(double longitude) {
		Longitude = longitude;
	}
	public static long getData() {
		return Data;
	}
	public static void setData(long data) {
		Data = data;
	}
	public static float getVelocidade() {
		return Velocidade;
	}
	public static void setVelocidade(float velocidade) {
		Velocidade = velocidade;
	}
	public static float getQualidade() {
		return Qualidade;
	}
	public static void setQualidade(float qualidade) {
		Qualidade = qualidade;
	}
	
	
	
	
	

}
