package org.bitvault.appstore.cloud.user.admin.service;

public interface DeleteService {

void deleteUsersByUserId(String userId ,String roleName);

void deleteSubDeveloper(String userId);
void deleteApplicationData(Integer applicationId);
 String deleteDocument(String userId);
 String deleteDocumentApplication(String applicationId);
 String deleteDocumentAppApplication(String AppApplicationId);
}
