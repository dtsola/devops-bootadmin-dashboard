/*
 * Copyright 2022 Dtsola.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dtsola.oss.devops.adminboot.dashboard.controller;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>Title: Devops BootAdmin Dashboard</p>
 * <p>Description: Promethues service discover adapter api</p>
 *
 * @author dtsola
 * @version 1.0
 */
@RestController
@RequestMapping("/api/prometheus")
public class PrometheusController {

    private static final Gson gson = new Gson();
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * Description: Get servie's infos from the registry center and
     * conversion format adaptation the http_sd_configs
     * *
     */
    @GetMapping("/service/list")
    @ResponseBody
    public String serviceListAction() {
        /*
        Format Style :
        [
                {
                    "targets": [ "<host>", ... ],
                    "labels": {
                    "<labelname>": "<labelvalue>", ...
                }
                },
          ...
        ]
        */
        final List<Map> r = new ArrayList<>();

        discoveryClient.getServices().forEach(service -> {
            final Map info = new HashMap<>();
            info.put("Targets", new ArrayList<String>());
            info.put("Labels", new HashMap<String, String>());
            discoveryClient.getInstances(service).forEach(instance -> {
                ((List<String>) info.get("Targets")).add(instance.getHost() + ":" + instance.getPort());
            });

            r.add(info);
        });

        return gson.toJson(r);
    }

}




