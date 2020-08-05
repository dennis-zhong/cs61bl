package bearmaps;

import bearmaps.utils.Constants;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AugmentedStreetMapGraphTest {

    @Test
    public void getLocations() {
        AugmentedStreetMapGraph graph = new AugmentedStreetMapGraph(Constants.OSM_DB_PATH);
        List<Map<String, Object>> lst = graph.getLocations("");
    }
}