//
//  GamesViewController.h
//  HybridPlayCenter
//
//  Created by n3m3da on 28/4/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "XMLRPCConnection.h"
#import "XMLRPCConnectionDelegate.h"
#import "XMLRPCConnectionManager.h"
#import "XMLRPCResponse.h"
#import "XMLRPCRequest.h"

#import "AppDelegate.h"
#import "PageContentViewController.h"

#define USERID @"HPG3RPCxmLUSEr"
#define USERPW @"0n87umÇwpk4coTURXzn?o^w"
#define HPWEBSITE @"http://games.hybridplay.com/"
#define XMLRPCURL @"http://games.hybridplay.com/index.php?hp_xmlrpc=true"

@interface GamesViewController : UIViewController <UIPageViewControllerDataSource> {
    
    UIBarButtonItem *revealButtonItem;
    
    AppDelegate*                appDelegate;
    NSManagedObjectContext*     context;
    
    BOOL                        haveInternet;
    
}

// -----------------------------------------------------------------------------
@property (strong, nonatomic) UIPageViewController *pageViewController;
@property (strong, nonatomic) NSArray *pageTitles;
@property (strong, nonatomic) NSArray *pageImages;
@property (strong, nonatomic) NSArray *pageDescriptions;
@property (strong, nonatomic) NSArray *pageStoreLinks;



@end
