package com.fidelreactlibrary;

import android.app.Activity;

import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.WritableMap;
import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultError;
import com.fidel.sdk.LinkResultErrorCode;
import com.fidelreactlibrary.events.CallbackActivityEventListener;
import com.fidelreactlibrary.fakes.CallbackSpy;
import com.fidelreactlibrary.fakes.DataConverterStub;
import com.fidelreactlibrary.fakes.DataProcessorSpy;
import com.fidelreactlibrary.fakes.IntentMock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class CallbackActivityEventListenerTests {

    private CallbackActivityEventListener sut;
    private CallbackSpy callbackSpy;
    private DataConverterStub<Object, WritableMap> linkResultConverterStub;
    private Activity activity;
    private IntentMock<LinkResult> intent;
    private DataProcessorSpy<WritableMap> errorHandlerSpy;

    private static final String RESULT_EXTRA_KEY = Fidel.FIDEL_LINK_CARD_RESULT_CARD;
    private static final int REQUEST_CODE = Fidel.FIDEL_LINK_CARD_REQUEST_CODE;
    private static final LinkResultError testLinkResultError
            = new LinkResultError(LinkResultErrorCode.INVALID_URL, "Test message", "some date");
    private static final LinkResult testLinkResult = new LinkResult("TEST CARD ID");

    @Before
    public final void setUp() {
        linkResultConverterStub = new DataConverterStub<>();
        errorHandlerSpy = new DataProcessorSpy<>();
        sut = new CallbackActivityEventListener(linkResultConverterStub, errorHandlerSpy);
        callbackSpy = new CallbackSpy();
        sut.callbackIsReady(callbackSpy);

        activity = Robolectric.buildActivity(Activity.class).setup().get();
        intent = new IntentMock<>(activity, Activity.class);
        intent.parcelableExtraToReturn = testLinkResult;
        linkResultConverterStub.convertedDataToReturn = new JavaOnlyMap();
    }

    @After
    public final void tearDown() {
        sut = null;
        callbackSpy = null;
        linkResultConverterStub = null;
        activity = null;
        intent = null;
        errorHandlerSpy = null;
    }

    @Test
    public void test_WhenReceivingLinkResult_SendItConvertedInCallback() {
        sut.onActivityResult(activity, REQUEST_CODE, 0, intent);

        assertEquals(RESULT_EXTRA_KEY, intent.parcelableExtraNameAskedFor);
        assertTrue(callbackSpy.didInvoke);
        assertEquals(linkResultConverterStub.convertedDataToReturn, callbackSpy.receivedResultMap);
    }

    @Test
    public void test_WhenReceivingLinkResultWithError_DontCallCallback() {
        onActivityResultWithError();
        assertFalse(callbackSpy.didInvoke);
    }

    @Test
    public void test_WhenReceivingLinkResultWithError_SendConvertedErrorToItsHandler() {
        onActivityResultWithError();
        linkResultConverterStub.convertedDataToReturn = new JavaOnlyMap();
        assertEquals("When returning error it should be converted",
                intent.parcelableExtraToReturn.getError(), linkResultConverterStub.dataReceived);
        assertEquals("When returning error send converted error to it's handler",
                linkResultConverterStub.convertedDataToReturn, errorHandlerSpy.dataToProcess);
    }
    
    @Test
    public void test_WhenRequestCodeIsNotFidelRequestCode_DontInvokeTheCallback() {
        sut.onActivityResult(activity,0, 0, intent);
        assertFalse(callbackSpy.didInvoke);
    }

    @Test
    public void test_WhenRequestCodeIsNotFidelRequestCode_DontAskForFidelExtras() {
        sut.onActivityResult(activity,0, 0, intent);
        assertNull(intent.parcelableExtraNameAskedFor);
    }

    @Test
    public void test_WhenCardLinkingFails_SendConvertedLinkResultToErrorHandler() {
        sut.onCardLinkingFailed(testLinkResultError);
        assertEquals(linkResultConverterStub.convertedDataToReturn, errorHandlerSpy.dataToProcess);
    }

    @Test
    public void test_WhenCardLinkingFails_ConvertTheErrorToWritableMap() {
        sut.onCardLinkingFailed(testLinkResultError);
        assertEquals(linkResultConverterStub.dataReceived, testLinkResultError);
    }

    @Test
    public void test_WhenCardLinkingSucceeds_DoNotCallErrorHandler() {
        sut.onCardLinkingSucceeded(testLinkResult);
        assertNull(errorHandlerSpy.dataToProcess);
    }

    @Test
    public void test_WhenCardLinkingSucceeds_DoNotCallCallback() {
        sut.onCardLinkingSucceeded(testLinkResult);
        assertNull(callbackSpy.receivedErrorMap);
        assertNull(callbackSpy.receivedResultMap);
    }

    private void onActivityResultWithError() {
        LinkResultErrorCode errorCode = LinkResultErrorCode.USER_CANCELED;
        intent.parcelableExtraToReturn = new LinkResult(errorCode, "Test error message", "2021-05-19T12:37:55.278Z");
        sut.onActivityResult(activity, REQUEST_CODE, 0, intent);
    }
}
