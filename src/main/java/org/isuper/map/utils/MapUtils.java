/**
 * 
 */
package org.isuper.map.utils;

/**
 * @author Super Wang
 *
 */
public class MapUtils {

	/**
	 * Transfer from earth(WGS-84) to mars(GCJ-02).
	 * 
	 * @param wgsLat
	 * 			The latitude of a WGS coordinate
	 * @param wgsLng
	 * 			The longitude of a WGS coordinate
	 * @return
	 * 			An {@link LatLng} mars coordinate
	 */
	public static LatLng wgs2gcj(double wgsLat, double wgsLng) {
		if (outOfChina(wgsLat, wgsLng)) {
			return new LatLng(wgsLat, wgsLng);
		}
		LatLng d = delta(wgsLat, wgsLng);
		return new LatLng(wgsLat + d.lat, wgsLng + d.lng);
	}

	/**
	 * Transfer from mars(GCJ-02) to earth(WGS-84).
	 * 
	 * @param gcjLat
	 * 			The latitude of a GCJ coordinate
	 * @param gcjLng
	 * 			The longitude of a GCJ coordinate
	 * @return
	 * 			An {@link LatLng} earth coordinate
	 */
	public static LatLng gcj2wgs(float gcjLat, float gcjLng) {
		if (outOfChina(gcjLat, gcjLng)) {
			return new LatLng(gcjLat, gcjLng);
		}
		LatLng d = delta(gcjLat, gcjLng);
		return new LatLng(gcjLat - d.lat, gcjLng - d.lng);
	}

	private static boolean outOfChina(double lat, double lng) {
		if ((lng < 72.004) || (lng > 137.8347)) {
			return true;
		}
		if ((lat < 0.8293) || (lat > 55.8271)) {
			return true;
		}
		return false;
	}

	private static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
		return ret;
	}

	private static LatLng delta(double lat, double lng) {
		double a = 6378245.0;
		double ee = 0.00669342162296594323;
		double dLat = transformLat(lng - 105.0, lat - 35.0);
		double dLng = transformLon(lng - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * Math.PI;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
		dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
		return new LatLng(dLat, dLng);
	}

}
