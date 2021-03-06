/*
 * Copyright 2015 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hannesdorfmann.mosby.sample.dagger1.members;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.hannesdorfmann.mosby.sample.dagger1.model.GithubApi;
import com.hannesdorfmann.mosby.sample.dagger1.model.NetworkException;
import com.hannesdorfmann.mosby.sample.dagger1.model.User;
import java.util.List;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Hannes Dorfmann
 */
public class MembersPresenter extends MvpNullObjectBasePresenter<MembersView> {

  private GithubApi githubApi;

  @Inject public MembersPresenter(GithubApi githubApi) {
    this.githubApi = githubApi;
  }

  public void loadSquareMembers(final boolean pullToRefresh) {

    getView().showLoading(pullToRefresh);

    githubApi.getMembers("square", new Callback<List<User>>() {
      @Override public void success(List<User> users, Response response) {
        // needs no isViewAttached() check since it's a NullObjectBasePresenter --> see javadoc
        getView().setData(users);
        getView().showContent();
      }

      @Override public void failure(RetrofitError error) {
        // needs no isViewAttached() check since it's a NullObjectBasePresenter --> see javadoc
        Throwable throwable;
        if (error.getKind() == RetrofitError.Kind.NETWORK) {
          throwable = new NetworkException();
        } else {
          throwable = error.getCause();
        }
        getView().showError(throwable, pullToRefresh);
      }
    });
  }
}
