package ru.belogurow.socialnetworkclient.users.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.dto.UserProfileDto;
import ru.belogurow.socialnetworkclient.users.model.UserProfile;
import ru.belogurow.socialnetworkclient.users.repository.RemoteUserProfileRepository;

public class UserProfileViewModel extends ViewModel {
    private static final String TAG = UserProfileViewModel.class.getSimpleName();

    @Inject
    protected RemoteUserProfileRepository mRemoteUserProfileRepository;

    private CompositeDisposable mCompositeDisposable;

    public UserProfileViewModel() {
        App.getComponent().inject(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    public LiveData<Resource<UserProfileDto>> addNewProfile(UUID userId, UserProfile userProfile) {
        Log.d(TAG, "addNewProfile: " + userId + " " + userProfile);

        final MutableLiveData<Resource<UserProfileDto>> liveData = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteUserProfileRepository.addUserProfile(userId, userProfile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userProfileDto -> {
                                    Log.d(TAG, "addNewProfile-result: " + userProfileDto.toString());
                                    liveData.postValue(Resource.success(userProfileDto));
                                },
                                error -> {
                                    Log.d(TAG, "addNewProfile-error: " + error.getMessage());

                                    if (error instanceof HttpException)
                                        liveData.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        liveData.postValue(Resource.error(error.getMessage()));
                                    }
                                })
        );

        return liveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
