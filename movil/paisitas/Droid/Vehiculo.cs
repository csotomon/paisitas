using System;
namespace paisitas.Droid
{
	public class Vehiculo
	{
		public Coordenada coordenadas
		{
			get;
			set;
		}
		public string nombre
		{
			get;
			set;
		}
		public string placas
		{
			get;
			set;
		}
		public long ultimaConexion
		{
			get;
			set;
		}
	}

	public class Conexiones {
		bool valor;
	}
}

