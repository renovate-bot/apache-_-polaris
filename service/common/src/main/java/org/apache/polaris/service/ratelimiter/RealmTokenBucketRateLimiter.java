/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.polaris.service.ratelimiter;

import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.apache.polaris.core.context.RealmContext;

/**
 * Rate limiter that maps the request's realm identifier to its own TokenBucket, with its own
 * capacity.
 */
@Identifier("default")
@RequestScoped
public class RealmTokenBucketRateLimiter implements RateLimiter {

  private final TokenBucketFactory tokenBucketFactory;
  private final RealmContext realmContext;

  @Inject
  public RealmTokenBucketRateLimiter(
      TokenBucketFactory tokenBucketFactory, RealmContext realmContext) {
    this.tokenBucketFactory = tokenBucketFactory;
    this.realmContext = realmContext;
  }

  /**
   * This signifies that a request is being made. That is, the rate limiter should count the request
   * at this point.
   *
   * @return Whether the request is allowed to proceed by the rate limiter
   */
  @Override
  public boolean canProceed() {
    return tokenBucketFactory.getOrCreateTokenBucket(realmContext).tryAcquire();
  }
}
