package ru.belogurow.socialnetworkclient.chat.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.chat.model.FileEntity;
import ru.belogurow.socialnetworkclient.chat.repository.RemoteFileRepository;
import ru.belogurow.socialnetworkclient.common.web.Resource;

public class FileViewModel  extends ViewModel {

    private static final String TAG = FileViewModel.class.getSimpleName();

    private CompositeDisposable mCompositeDisposable;

    @Inject
    protected RemoteFileRepository mRemoteFileRepository;

    public FileViewModel() {
        App.getComponent().inject(this);
        mCompositeDisposable = new CompositeDisposable();
    }


    public LiveData<Resource<FileEntityDto>> uploadAvatar(UUID userId, FileEntity fileEntity, File file) {
        Log.d(TAG, "uploadAvatar: " + userId);

        final MutableLiveData<Resource<FileEntityDto>> resourceMutableLiveData = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteFileRepository.uploadAvatar(userId, fileEntity, file)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                 avatarResource -> {
                                     Log.d(TAG, "uploadAvatar-result: " + avatarResource);
                                     resourceMutableLiveData.postValue(Resource.success(avatarResource));
                                },
                                error -> {
                                    Log.d(TAG, "uploadAvatar-error: " + error.toString());

                                    if (error instanceof HttpException)
                                        resourceMutableLiveData.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        resourceMutableLiveData.postValue(Resource.error(error.getMessage()));
                                    }
                                })
        );

        return resourceMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
