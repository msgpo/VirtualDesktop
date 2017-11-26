/* -*- Mode: C; c-basic-offset: 4; indent-tabs-mode: nil -*- */
/*
   Copyright (C) 2010 Red Hat, Inc.

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, see <http://www.gnu.org/licenses/>.
*/
#ifndef SPICE_UTIL_H
#define SPICE_UTIL_H

#include <glib.h>
#include<android/log.h> 

G_BEGIN_DECLS

void spice_util_set_debug(gboolean enabled);
gboolean spice_util_get_debug(void);
const gchar *spice_util_get_version_string(void);

#define SPICE_DEBUG(fmt, ...)                                   \
    do {                                                        \
	if (1 || G_UNLIKELY(spice_util_get_debug()))                 \
	__android_log_print(ANDROID_LOG_ERROR,"----spice-android----",fmt, ##__VA_ARGS__);\
    } while (0)

//g_debug(G_STRLOC " " fmt, ## __VA_ARGS__);          \
//
#define SPICE_RESERVED_PADDING 44

G_END_DECLS

#endif /* SPICE_UTIL_H */
