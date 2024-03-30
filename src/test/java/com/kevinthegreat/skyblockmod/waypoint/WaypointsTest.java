package com.kevinthegreat.skyblockmod.waypoint;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class WaypointsTest {
    @Test
    void testFromSkytilsBase64() {
        String waypointCategoriesSkytilsBase64 = "eyJjYXRlZ29yaWVzIjpbeyJuYW1lIjoiY2F0ZWdvcnkiLCJ3YXlwb2ludHMiOlt7Im5hbWUiOiJ3YXlwb2ludCIsIngiOjAsInkiOjAsInoiOjAsImVuYWJsZWQiOmZhbHNlLCJjb2xvciI6MzMwMjMsImFkZGVkQXQiOjF9LHsibmFtZSI6MSwieCI6LTEsInkiOjAsInoiOjEsImVuYWJsZWQiOnRydWUsImNvbG9yIjowLCJhZGRlZEF0IjoxfV0sImlzbGFuZCI6Imh1YiJ9XX0=";
        List<WaypointCategory> waypointCategories = Waypoints.fromSkytilsBase64(waypointCategoriesSkytilsBase64);
        List<WaypointCategory> expectedWaypointCategories = List.of(new WaypointCategory("category", "hub", List.of(new NamedWaypoint(BlockPos.ORIGIN, "waypoint", new float[]{0f, 0.5019608f, 1f}, false), new NamedWaypoint(new BlockPos(-1, 0, 1), "1", new float[]{0f, 0f, 0f}, true))));

        Assertions.assertEquals(expectedWaypointCategories, waypointCategories);
    }

    @Test
    void testToSkytilsBase64() {
        List<WaypointCategory> waypointCategories = List.of(new WaypointCategory("category", "hub", List.of(new NamedWaypoint(BlockPos.ORIGIN, "waypoint", new float[]{0f, 0.5f, 1f}, false), new NamedWaypoint(new BlockPos(-1, 0, 1), "1", new float[]{0f, 0f, 0f}, true))));
        String waypointCategoriesSkytilsBase64 = Waypoints.toSkytilsBase64(waypointCategories);
        String expectedWaypointCategoriesSkytilsBase64 = "eyJjYXRlZ29yaWVzIjpbeyJuYW1lIjoiY2F0ZWdvcnkiLCJpc2xhbmQiOiJodWIiLCJ3YXlwb2ludHMiOlt7Im5hbWUiOiJ3YXlwb2ludCIsImNvbG9yIjoyMTMwNzM5MTk5LCJlbmFibGVkIjpmYWxzZSwieCI6MCwieSI6MCwieiI6MH0seyJuYW1lIjoiMSIsImNvbG9yIjoyMTMwNzA2NDMyLCJlbmFibGVkIjp0cnVlLCJ4IjotMSwieSI6MCwieiI6MX1dfV19";

        Assertions.assertEquals(expectedWaypointCategoriesSkytilsBase64, waypointCategoriesSkytilsBase64);
    }
}
