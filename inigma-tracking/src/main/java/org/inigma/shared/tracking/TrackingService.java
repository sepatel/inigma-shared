package org.inigma.shared.tracking;

import org.inigma.shared.webapp.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TrackingService extends RestService {
    @RequestMapping(value = "/inigma/trackingNumber/{trackingNumber}", method = RequestMethod.GET)
    public ResponseEntity<TrackingResponse> getMessage(@PathVariable String trackingNumber) {
        TrackingResponse response = createResponse(TrackingResponse.class);
        response.setNumber(trackingNumber);
        response.setType(TrackingUtil.getTrackingType(trackingNumber));
        if (response.getType() == null) {
            reject("invalidTrackingNumber");
            stopOnRejections();
        }

        switch (response.getType()) { // convenience url for known types to help reduce consumers workload a bit
            case FedEx:
                response.setUrl("http://www.fedex.com/Tracking?language=english&cntry_code=us&tracknumbers=" + trackingNumber);
                break;
            case UPS:
                response.setUrl("http://wwwapps.ups.com/WebTracking/processInputRequest?TypeOfInquiryNumber=T&InquiryNumber1=" + trackingNumber);
                break;
            case USPS:
                response.setUrl("https://tools.usps.com/go/TrackConfirmAction?qtc_tLabels1=" + trackingNumber);
                break;
        }

        return response();
    }
}
