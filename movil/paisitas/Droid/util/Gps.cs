using System;
using Android.Runtime;
using Android.OS;
using Android.Locations;
using Android.Content;
using System.Collections.Generic;
using Android.Util;
using Android.App;

namespace paisitas.Droid.util
{
	public class Gps : Java.Lang.Object, ILocationListener
	{

		Location _currentLocation;
		LocationManager _locationManager;

		string _locationProvider;

		public object currentLocation
		{
			get { return _currentLocation;}
		}

		private ILocationListener _evento;



		public void activarGPS(Context contexto, ILocationListener evento) 
		{
			_evento = evento;
			Criteria locationCriteria = new Criteria();

			locationCriteria.Accuracy = Accuracy.Coarse;
			locationCriteria.PowerRequirement = Power.Medium;

			_locationManager = (LocationManager)contexto.GetSystemService(Context.LocationService);

			_locationProvider = _locationManager.GetBestProvider(locationCriteria, true);

			if (_locationProvider != null)
			{
				_locationManager.RequestLocationUpdates(_locationProvider, 2000, 1, this);
			}
			else
			{
				//Log.Info(TAG, "No location providers available");
				Log.Info("prueba", "no se encontraron providers");
			}
		}


		public void OnLocationChanged(Location location)
		{
			_currentLocation = location;
			_evento.OnLocationChanged(location);
		}

		public void OnProviderDisabled(string provider)
		{
			_evento.OnProviderDisabled(provider);
		}

		public void OnProviderEnabled(string provider)
		{
			_evento.OnProviderEnabled(provider);
		}

		public void OnStatusChanged(string provider, [GeneratedEnum] Availability status, Bundle extras)
		{
			_evento.OnStatusChanged(provider, status, extras);
		}
	}
}

