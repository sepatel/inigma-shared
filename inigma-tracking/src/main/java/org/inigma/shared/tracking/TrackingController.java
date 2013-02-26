package org.inigma.shared.tracking;

import org.inigma.shared.webapp.AjaxController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/inigma")
public class TrackingController extends AjaxController {
    @RequestMapping(value = "/trackingNumber/{trackingNumber}", method = RequestMethod.GET)
    @ResponseBody
    public TrackingResponse getMessage(@PathVariable String trackingNumber) {
        TrackingType type = TrackingUtil.getTrackingType(trackingNumber);
        TrackingResponse response = new TrackingResponse();
        response.setType(type);
        response.setNumber(trackingNumber);

        if (type != null) {
            switch (type) { // convenience url for known types to help reduce consumers workload a bit
            case FedEx:
                response.setUrl("http://www.fedex.com/Tracking?language=english&cntry_code=us&tracknumbers="
                        + trackingNumber);
                break;
            case UPS:
                response.setUrl("http://wwwapps.ups.com/WebTracking/processInputRequest?TypeOfInquiryNumber=T&InquiryNumber1="
                        + trackingNumber);
                break;
            case USPS:
                response.setUrl("https://tools.usps.com/go/TrackConfirmAction?qtc_tLabels1=" + trackingNumber);
                break;
            }
        }

        return response;
    }
}
