import static org.fest.assertions.Assertions.assertThat;
import org.junit.*;


public class BinaryUpgraderTest {

	@Test
	public void compareVerison() {
		//Given
		String localVersion1 = "1.2";
		String remoteVersion1 = "2.4";
		String localVersion2 = "1.3";
		String remoteVersion2 = "1.3";
		String localVersion3 = "2.4";
		String remoteVersion3 = "1.5";
		
		//When
		int compare1 = BinaryUpgrader.compareVersion(localVersion1, remoteVersion1);
		int compare2 = BinaryUpgrader.compareVersion(localVersion2, remoteVersion2);
		int compare3 = BinaryUpgrader.compareVersion(localVersion3, remoteVersion3);
		
		//Then
		assertThat(compare1).isEqualTo(1);
		assertThat(compare2).isEqualTo(0);
		assertThat(compare3).isEqualTo(-1);
		
	}

}
