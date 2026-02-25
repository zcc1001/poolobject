/**
 * 
 */
package ubu.gii.dass.c01;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#getInstance()}.
	 */
	@Test
	@DisplayName("testGetInstance")
	public void testGetInstance() {
		ReusablePool  instance = ReusablePool.getInstance();
		Assertions.assertNotNull(instance);
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
	/**
	 * Test method for
	 * {@link ubu.gii.dass.c01.ReusablePool#releaseReusable(ubu.gii.dass.c01.Reusable)}.
	 */
	@Test
	@DisplayName("testReleaseReusable")
	@Disabled("Not implemented yet")
	public void testReleaseReusable() {

	}

}
