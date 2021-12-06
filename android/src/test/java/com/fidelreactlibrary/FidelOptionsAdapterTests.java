package com.fidelreactlibrary;

import com.fidelapi.Fidel;
import com.fidelreactlibrary.adapters.FidelOptionsAdapter;
import com.fidelreactlibrary.fakes.ReadableMapStub;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;


// Custom test runner is necessary for being able to use JSONObject
@RunWith(RobolectricTestRunner.class)
public class FidelOptionsAdapterTests {

    private ReadableMapStub map;
    private FidelOptionsAdapter sut = new FidelOptionsAdapter();

    private static final String TEST_DELETE_INSTRUCTIONS = "Test Delete instructions.";

    @After
    public final void tearDown() {
        sut = null;
        Fidel.deleteInstructions = null;
    }

    //Tests when keys are present, but no data is found for that key

    @Test
    public void test_IfHasDeleteInstructionsKeyButNoValue_DoNotSetThemToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        map = ReadableMapStub.mapWithExistingKeyButNoValue(keyToTestFor);
        processWithString(TEST_DELETE_INSTRUCTIONS, keyToTestFor);
        assertNotEqualsString(keyToTestFor, Fidel.deleteInstructions);
    }

    //Tests when keys are missing

    @Test
    public void test_IfDoesNotHaveAutoScanKey_DoNotSetItToTheSDK() {
        map = ReadableMapStub.mapWithNoKey();
        map.boolToReturn = true;
        sut.process(map);
        assertFalse(Fidel.shouldAutoScanCard);
    }

    @Test
    public void test_IfDoesNotHaveDeleteInstructionsKey_DoNotSetThemToTheSDK() {
        map = ReadableMapStub.mapWithNoKey();
        String key = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        processWithString(TEST_DELETE_INSTRUCTIONS, key);
        assertNotEqualsString(key, Fidel.deleteInstructions);
    }

    //Setting correct values tests

    @Test
    public void test_WhenDeleteInstructionsValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        map = ReadableMapStub.mapWithExistingKey(keyToTestFor);
        processWithString(TEST_DELETE_INSTRUCTIONS, keyToTestFor);
        assertEqualsString(keyToTestFor, Fidel.deleteInstructions);
    }

    //Helper functions

    private void processWithString(String string, String key) {
        map.stringForKeyToReturn.put(key, string);
        sut.process(map);
    }

    private void assertEqualsString(String key, String valueToCheckWith) {
        sut.process(map);
        assertEquals(map.stringForKeyToReturn.get(key), valueToCheckWith);
    }

    private void assertNotEqualsString(String key, String valueToCheckWith) {
        sut.process(map);
        assertNotEquals(map.stringForKeyToReturn.get(key), valueToCheckWith);
    }
}
