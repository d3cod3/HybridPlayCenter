//
//  MenuViewController.h
//  HybridPlayCenter
//
//  Created by n3m3da on 28/4/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AppDelegate.h"

@interface SWUITableViewCell : UITableViewCell
    @property (nonatomic) IBOutlet UILabel *label;
@end

@interface MenuViewController : UITableViewController {
    
    AppDelegate*                appDelegate;
    NSManagedObjectContext*     context;
    
}

// -----------------------------------------------------------------------------


@end
