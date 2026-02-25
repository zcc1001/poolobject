/**
 * 
 */
package ubu.gii.dass.c01;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;

/**
 * @author alumno
 *
 */
public class ReusablePoolTest {

	@BeforeAll
	public static void setUp() {
	}

	@AfterAll
	public static void tearDown() throws Exception {
	}

	@BeforeEach
	public void resetPool() {
		/**
		 * Reinicia el estado del pool antes de cada test:
		 * adquiere todas las instancias disponibles y las libera.
		 * Esto evita efectos colaterales entre pruebas al dejar el pool lleno.
		 */
		ReusablePool instance = ReusablePool.getInstance();
		List<Reusable> acquired = new ArrayList<>();
		while (true) {
			try {
				acquired.add(instance.acquireReusable());
			} catch (NotFreeInstanceException e) {
				break;
			}
		}
		for (Reusable r : acquired) {
			try {
				instance.releaseReusable(r);
			} catch (DuplicatedInstanceException e) {
				Assertions.fail("El pool no deberia contener instancias duplicadas.");
			}
		}
	}

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#getInstance()}.
	 */
	@Test
	@DisplayName("testGetInstance")
	public void testGetInstance() {
		ReusablePool  instance = ReusablePool.getInstance();
		Assertions.assertNotNull(instance);
	}

	@Test
	@DisplayName("getInstance devuelve siempre la misma instancia (Singleton)")
	public void testGetInstanceIsSingleton() {
		/**
		 * Comprueba el comportamiento Singleton:
		 * dos llamadas a getInstance() deben devolver exactamente el mismo objeto.
		 */
		ReusablePool instance1 = ReusablePool.getInstance();
		ReusablePool instance2 = ReusablePool.getInstance();
		Assertions.assertSame(instance1, instance2);
	}

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#acquireReusable()}.
	 */
	@Test
    @DisplayName("Aquirir todos los objetos disponibles y lanzar excepcion cuando este vacio")
    public void testAcquireReusable() throws NotFreeInstanceException {
        ReusablePool instance = ReusablePool.getInstance();
        
        // El pool tiene tamaño 2 por defecto
        Reusable r1 = instance.acquireReusable();
        Reusable r2 = instance.acquireReusable();
        
        Assertions.assertNotNull(r1);
        Assertions.assertNotNull(r2);
        Assertions.assertNotSame(r1, r2); // Verificamos que son objetos distintos

        // El tercer intento debe lanzar la excepción NotFreeInstanceException
        Assertions.assertThrows(NotFreeInstanceException.class, () -> {
            instance.acquireReusable();
        });
    }

	@Test
	@DisplayName("Reutilizar un objeto liberado devuelve la misma instancia")
	public void testReuseReleasedInstance() throws NotFreeInstanceException, DuplicatedInstanceException {
		/**
		 * Verifica que un objeto liberado vuelve a estar disponible en el pool
		 * y que se reutiliza la misma instancia al adquirirlo de nuevo.
		 */
		ReusablePool instance = ReusablePool.getInstance();

		Reusable r1 = instance.acquireReusable();
		instance.releaseReusable(r1);

		Reusable r2 = instance.acquireReusable();
		Assertions.assertSame(r1, r2);
	}

	@Test
	@DisplayName("Tras liberar todas las instancias, el pool vuelve a estar disponible")
	public void testPoolRestoresAfterRelease() throws NotFreeInstanceException, DuplicatedInstanceException {
		/**
		 * Valida el ciclo completo:
		 * 1) se agotan las instancias del pool,
		 * 2) se liberan todas,
		 * 3) se pueden adquirir de nuevo y el pool vuelve a agotarse.
		 */
		ReusablePool instance = ReusablePool.getInstance();

		Reusable r1 = instance.acquireReusable();
		Reusable r2 = instance.acquireReusable();

		instance.releaseReusable(r1);
		instance.releaseReusable(r2);

		Reusable r3 = instance.acquireReusable();
		Reusable r4 = instance.acquireReusable();

		Assertions.assertNotNull(r3);
		Assertions.assertNotNull(r4);
		Assertions.assertNotSame(r3, r4);

		Assertions.assertThrows(NotFreeInstanceException.class, () -> {
			instance.acquireReusable();
		});
	}
	/**
	 * Test method for
	 * {@link ubu.gii.dass.c01.ReusablePool#releaseReusable(ubu.gii.dass.c01.Reusable)}.
	 */
	@Test
    @DisplayName("Liberar un objeto correctamente y lanzar excepcion si se intenta liberar uno ya existente")
    public void testReleaseReusable() throws NotFreeInstanceException, DuplicatedInstanceException {
        ReusablePool instance = ReusablePool.getInstance();
        
        // 1. Preparamos el escenario: sacamos un objeto para luego devolverlo
        Reusable r1 = instance.acquireReusable();
        
        // 2. Caso de éxito: Devolvemos el objeto y no debería dar error
        instance.releaseReusable(r1);
        
        // 3. Caso de error: Intentamos devolver el MISMO objeto otra vez
        // Esto cubrirá la clase DuplicatedInstanceException
        Assertions.assertThrows(DuplicatedInstanceException.class, () -> {
            instance.releaseReusable(r1);
        });
    }

}
