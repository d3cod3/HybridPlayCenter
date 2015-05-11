//
//  AppDelegate.h
//  HybridPlayCenter
//
//  Created by n3m3da on 28/4/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

+ (AppDelegate *)sharedAppDelegate;

+ (BOOL)haveInternetConnection;


@end

