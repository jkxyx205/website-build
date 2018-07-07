package com.yodean.site.web.common.controller;

import com.rick.dev.dto.ResponseDTO;

/**
 * Created by rick on 2017/7/18.
 */
public class WebController {

    public ResponseDTO response(Object data) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(data);
        responseDTO.setStatus(ResponseDTO.STATUS_SUCCESS);
        return responseDTO;
    }

    public ResponseDTO response() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(ResponseDTO.STATUS_SUCCESS);
        return responseDTO;
    }

}
