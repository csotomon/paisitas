using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Locations;
using System.Collections.Generic;

namespace paisitas.Droid
{
	[Activity(Label = "paisitas", MainLauncher = true, Icon = "@mipmap/icon")]
	public class MainActivity : Activity
	{
		Location _currentLocation;
		LocationManager _locationManager;
		string _locationProvider;
		
		protected override void OnCreate(Bundle savedInstanceState)
		{
			base.OnCreate(savedInstanceState);

			// Set our view from the "main" layout resource
			SetContentView(Resource.Layout.Main);

			// Set our view from the "main" layout resource
			SetContentView(Resource.Layout.Main);

			Button botonDesconectar = FindViewById<Button>(Resource.Id.buttonDesconectar);
			Button botonConectar = FindViewById<Button>(Resource.Id.buttonConectar);
			TextView textoEstado = FindViewById<TextView>(Resource.Id.textViewEstado);

			botonDesconectar.Enabled = false;

			botonConectar.Click += (object sender, EventArgs e) =>
			{
				botonDesconectar.Enabled = true;
				botonConectar.Enabled = false;
				textoEstado.Text = "Conectando";
			};

			botonDesconectar.Click += (object sender, EventArgs e) =>
			{
				botonDesconectar.Enabled = false;
				botonConectar.Enabled = true;
				textoEstado.Text = "Desconectado";
			};

			InitializeLocationManager();
		}

		void InitializeLocationManager()
		{
			_locationManager = (LocationManager)GetSystemService(LocationService);

			Criteria criteriaForLocationService = new Criteria
			{
				Accuracy = Accuracy.Fine
			};
			IList<string> acceptableLocationProviders = _locationManager.GetProviders(criteriaForLocationService, true);

			if (acceptableLocationProviders.Count>0)
			{
				_locationProvider = acceptableLocationProviders[0];
			}
			else
			{
				_locationProvider = string.Empty;
			}

		}


	}
}


