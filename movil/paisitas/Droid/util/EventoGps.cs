using System;
using Android.Locations;
using Android.OS;
using Android.Runtime;

namespace paisitas.Droid.util
{
	public class EventoGps: Java.Lang.Object, ILocationListener
	{
		public delegate void OnLocationHandler(Location location);
		public event OnLocationHandler locationChangedEvent;

		public delegate void OnProviderDisabledHandler(string provider);
		public event OnProviderDisabledHandler providerDisabledEvent;

		public delegate void OnProviderEnabledHandler(string provider);
		public event OnProviderEnabledHandler providerEnabledEvent;

		public delegate void OnStatusChangedHandler(string provider, [GeneratedEnum] Availability status, Bundle extras);
		public event OnStatusChangedHandler statusChangedEvent;

		public void OnLocationChanged(Location location)
		{
			if (locationChangedEvent != null)
				locationChangedEvent(location);
		}

		public void OnProviderDisabled(string provider)
		{
			if (providerDisabledEvent != null)
				providerDisabledEvent(provider);
		}

		public void OnProviderEnabled(string provider)
		{
			if (providerEnabledEvent != null)
				providerEnabledEvent(provider);
		}

		public void OnStatusChanged(string provider, [GeneratedEnum] Availability status, Bundle extras)
		{
			if (statusChangedEvent != null)
				statusChangedEvent(provider, status, extras);
		}
	}
}