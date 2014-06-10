package dojo;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Assert;
import org.junit.Test;

import dojo.service.IlegalQuantityException;
import dojo.service.LibroService;

/*
 * Hay una saga que tiene 5 libros en total. Como está de moda, existe una promoción por comprar los distintos libros de la saga
 * 
 * Cada una de las copias cuesta X pesos, sin importar de cual de los 5 libros se trata. Si comprás 2 libros distintos, obtenés un A% de descuento.
 * Si comprás 3, B% de descuento. 4 libros distintos, C% de descuento.  5 libros, D% de descuento.
 *  
 * Crear un algoritmo que dado una compra de libros, te devuelva el precio total calculando el mayor descuento posible.
 * 
 * Ejemplo: [0,1,1,2,2,2] (un libro de la clase 0, dos de la clase 1 y tres de la clase 2 )
 * El precio sería = (precio de 0+1+2 con un B% de descuento) + (precio de 1+2 con un A% de descuento) + (precio de 2)
 * 
 * Tanto el valor X del libro, como los valores A,B,C y D de los descuentos se obtienen mediante una llamada a los servicios
 * expuestos en LibroService.
 * 
 * El algoritmo debe contemplar que el método del servicio que calcula el descuento para una cantidad dada puede levantar una excepción,
 * y las cantidades para las cuales la levanta podrían variar en un futuro cercano
 * 
 */

public class KataTest {

	@Mocked
	LibroService libroService;

	Double calcularPrecioTotal(Integer[] libros, final LibroService libroService) throws IlegalQuantityException {
		double price = libroService.bookPrice() * libros.length;
		
		return price - price * libroService.discount(libros.length)/100;
	}

	@Test
	public void testGetBookPrice()
			throws IlegalQuantityException {
		new Expectations() {{
			libroService.bookPrice();
			result = 10D;
			libroService.discount(1);
			result = 0;
			
		}};

		Double bookPrice = calcularPrecioTotal(new Integer[] { 1 },
				libroService);
		Assert.assertEquals(new Double(10D), bookPrice);
	}

	@Test
	public void testGetBookPriceTwoBooks()
			throws IlegalQuantityException {
		new Expectations() {{
			libroService.bookPrice();
			result = 10D;
			libroService.discount(2);
			result = 10;
			
		}};

		Double bookPrice = calcularPrecioTotal(new Integer[] { 2, 2 },
				libroService);
		Assert.assertEquals(new Double(20D), bookPrice);
	}

	@Test
	public void testGetBookPriceTwoDifferentBooksWithDiscount()
			throws IlegalQuantityException {
		new Expectations() {{
			libroService.bookPrice();
			result = 10D;
			libroService.discount(2);
			result = 10;
			
		}};
		Double bookPrice = calcularPrecioTotal(new Integer[] { 1, 2 },
				libroService);
		Assert.assertEquals(new Double(18D), bookPrice);

	}
}
