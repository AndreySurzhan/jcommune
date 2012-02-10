/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jtalks.jcommune.web.interceptors;

import org.jtalks.jcommune.service.nontransactional.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Global interceptor works for all pages, clears location of
 * the current user. Location is a certain page user is viewing at the moment
 *
 * @author Andrey Kluev
 */
public class ClearInterceptor extends HandlerInterceptorAdapter {

    private LocationService locationService;

    /**
     * @param locationService to operate with current user location on forum
     */
    @Autowired
    public ClearInterceptor(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Clears location current user in forum
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param handler  handler
     * @return true, as processing should be continued anyway
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        /**
         * This condition is necessary because interceptors are designed to be called before a controller.
         * As avatars are requested from the separate controller avatar request will actually clear
         * location set on page. So, won't be able to see his name in a page visitors list.
         *
         * That is why we're skipping avatar requests when determining user location on forum.
         */
        if (!request.getRequestURI().endsWith("/avatar")) {
            // failure here should not cause all the web processing chain to be broken
            // todo: find a better solution
            try {
                locationService.clearUserLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}