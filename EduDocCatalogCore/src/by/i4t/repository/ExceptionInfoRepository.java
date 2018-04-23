package by.i4t.repository;

import by.i4t.objects.ExceptionInfo;

import java.util.List;

/**
 * Created by Ilya on 08.11.2016.
 */
public interface ExceptionInfoRepository extends BaseUUIDRepository<ExceptionInfo> {
    List<ExceptionInfo> findTop200ByOrderByExceptionDateDesc();
}
