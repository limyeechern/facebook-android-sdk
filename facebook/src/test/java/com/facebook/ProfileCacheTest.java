/*
 * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook;

import static org.junit.Assert.*;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(FacebookSdk.class)
public final class ProfileCacheTest extends FacebookPowerMockTestCase {
  @Before
  public void before() {
    PowerMockito.mockStatic(FacebookSdk.class);
    PowerMockito.when(FacebookSdk.getApplicationId()).thenReturn("123456789");
    PowerMockito.when(FacebookSdk.isInitialized()).thenReturn(true);

    Context context = ApplicationProvider.getApplicationContext();
    context
        .getSharedPreferences(ProfileCache.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit()
        .clear()
        .commit();
    PowerMockito.when(FacebookSdk.getApplicationContext()).thenReturn(context);
  }

  @Test
  public void testEmptyCase() {
    ProfileCache cache = new ProfileCache();
    assertNull(cache.load());
  }

  @Test
  public void testSaveGetAndClear() {
    ProfileCache cache = new ProfileCache();
    Profile profile1 = ProfileTest.createDefaultProfile();
    cache.save(profile1);
    Profile profile2 = cache.load();
    ProfileTest.assertDefaultObjectGetters(profile2);
    assertEquals(profile1, profile2);

    profile1 = ProfileTest.createMostlyNullsProfile();
    cache.save(profile1);
    profile2 = cache.load();
    ProfileTest.assertMostlyNullsObjectGetters(profile2);
    assertEquals(profile1, profile2);

    cache.clear();
    assertNull(cache.load());
  }
}
