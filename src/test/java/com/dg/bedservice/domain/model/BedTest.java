package com.dg.bedservice.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.dg.bedservice.domain.events.BaseEvent;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BedTest {

    @Test
    public void testConstruction() {
        Bed bedUnderTest = new Bed("testBed", "testLocation");
        this.updateState(bedUnderTest);

        assertEquals("Id does not match", "testBed", bedUnderTest.getId());
        assertEquals("Location does not match", "testLocation", bedUnderTest.getLocation());
        assertNull("Occupant should be null", bedUnderTest.getOccupantId());
        assertEquals("Status is not Available", "Available", bedUnderTest.getStatus());
    }

    @Test
    public void testBookingAvailableBed() {
        Bed bedUnderTest = new Bed("testBed", "testLocation");
        this.updateState(bedUnderTest);
        bedUnderTest.book("testOccupant");
        this.updateState(bedUnderTest);

        assertEquals("Id does not match", "testBed", bedUnderTest.getId());
        assertEquals("Location does not match", "testLocation", bedUnderTest.getLocation());
        assertEquals("Occupant does not match", "testOccupant", bedUnderTest.getOccupantId());
        assertEquals("Status is not Available", "Booked", bedUnderTest.getStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testBookingBookedBed() {
        Bed bedUnderTest = new Bed("testBed", "testLocation");
        this.updateState(bedUnderTest);
        bedUnderTest.book("testOccupant");
        this.updateState(bedUnderTest);
        bedUnderTest.book("testExtraOccupant");
        this.updateState(bedUnderTest);
    }

    private Bed updateState(Bed bed) {
        for (BaseEvent event : bed.getDirtyEvents()) {
            event.applyOn(bed);
        }
        bed.clearDirtyEvents();
        return bed;
    }
}