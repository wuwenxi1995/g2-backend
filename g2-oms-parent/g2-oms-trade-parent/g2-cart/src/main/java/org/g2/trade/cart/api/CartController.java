package org.g2.trade.cart.api;

import org.g2.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author wuwenxi 2023-06-12
 */
@RestController
@RequestMapping(value = "/v1/{organizationId}/cart")
public class CartController {

    @GetMapping("/count")
    public ResponseEntity<?> count(@PathVariable Long organizationId,
                                   @RequestParam(required = false, defaultValue = "false") Boolean asyncRefresh) {

        return Results.success();
    }
}
