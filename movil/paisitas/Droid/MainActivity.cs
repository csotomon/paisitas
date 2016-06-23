using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Locations;
using System.Collections.Generic;
using paisitas.Droid.util;
using Android.Util;
using FireSharp.Interfaces;
using FireSharp.Config;
using FireSharp;
using FireSharp.Response;
using Java.Util;

namespace paisitas.Droid
{
	[Activity(Label = "paisitas", MainLauncher = true, Icon = "@mipmap/icon")]
	public class MainActivity : Activity
	{
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

			RadioGroup grupoMotos = FindViewById<RadioGroup>(Resource.Id.radioGroupMotos);

			RadioButton botonSeleccionado;

			botonDesconectar.Enabled = false;



			botonConectar.Click += (object sender, EventArgs e) =>
			{
				botonDesconectar.Enabled = true;
				botonConectar.Enabled = false;
				textoEstado.Text = "Conectando";
				botonSeleccionado = FindViewById<RadioButton>(grupoMotos.CheckedRadioButtonId);
				getClave(botonSeleccionado.Text);
			};

			botonDesconectar.Click += (object sender, EventArgs e) =>
			{
				botonDesconectar.Enabled = false;
				botonConectar.Enabled = true;
				textoEstado.Text = "Desconectado";
			};

			Log.Info("test", "iniciando");

			EventoGps ev = new EventoGps();

			ev.locationChangedEvent += onLocationEvento;

			prueba();

			Gps _gps = new Gps();
			_gps.activarGPS(this, ev);
		}

		public static void onLocationEvento(Location location) {
			Console.WriteLine(location.ToString());
		}

		public async void prueba() { 
			IFirebaseConfig config = new FirebaseConfig
			{
				//AuthSecret = "your_firebase_secret",
				BasePath = "https://paisitas-1343.firebaseio.com/"
			};

			IFirebaseClient _client = new FirebaseClient(config);


			Vehiculo vehiculo = new Vehiculo
			{
				coordenadas = new Coordenada
				{
					latitud = 6.244860,
					longitud = -75.589327
				},

				nombre = "Paisita 2",
				placas = "MVG 505,"

			};

			//PushResponse r1 = await _client.PushAsync("vehiculos/", vehiculo);
			//Vehiculo re = r1.ResultAs<Vehiculo>();

			//QueryBuilder qb = QueryBuilder.New("orderBy=\"nombre\"&equalTo=\"Paisita 1\"");
			//qb.OrderBy("nombre").StartAt("Paisita 1").EndAt("Paisita 1");
			//string test = qb.ToQueryString();
			//string queryStr = $"&{qb.ToQueryString()}";
			FirebaseResponse response = await _client.GetAsync("vehiculos/-KKunARFX41tKfDXP4qg");
			//Vehiculo t1 = response.ResultAs<Vehiculo>();

			FirebaseResponse response2 = await _client.GetAsync("vehiculos/-KKunARFX41tKfDXP4qg/conexiones");
			Dictionary<string, bool> test = response2.ResultAs<Dictionary<string, bool>>();


			//Dictionary<string, Vehiculo> todo2 = response.ResultAs<Dictionary<string, Vehiculo>>(); //The response will contain the data being retreived
			Log.Info("fire", "");
		}

		void getClave(string seleccion) { 
			
		}

	}
}
