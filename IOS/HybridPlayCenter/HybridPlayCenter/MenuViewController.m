//
//  MenuViewController.m
//  HybridPlayCenter
//
//  Created by n3m3da on 28/4/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import "MenuViewController.h"

@implementation SWUITableViewCell
@end

@interface MenuViewController ()

@end

@implementation MenuViewController

- (void) prepareForSegue: (UIStoryboardSegue *) segue sender: (id) sender{
    
    [self getContext];
    
    // configure the destination view controller:
    if ( [sender isKindOfClass:[UITableViewCell class]] ){
        
        //UILabel* c = [(SWUITableViewCell *)sender label];
        //UINavigationController *navController = segue.destinationViewController;
        
        //NSLog(@"%@",c.text);
    }
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 6;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    switch ( indexPath.row )
    {
        case 0:
            CellIdentifier = @"Games";
            break;
        case 1:
            CellIdentifier = @"Bluetooth";
            break;
        case 2:
            CellIdentifier = @"Calibration";
            break;
        case 3:
            CellIdentifier = @"Instructions";
            break;
        case 4:
            CellIdentifier = @"Credits";
            break;
        case 5:
            CellIdentifier = @"Exit";
            break;
    }
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier forIndexPath: indexPath];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    
    if([cell.textLabel.text isEqualToString:@"Exit"]){
        exit(0);
    }
    
}

#pragma mark - Core Data

- (void)getContext{
    appDelegate = [AppDelegate sharedAppDelegate];
    context = appDelegate.managedObjectContext;
}

#pragma mark state preservation / restoration
- (void)encodeRestorableStateWithCoder:(NSCoder *)coder {
    NSLog(@"%s", __PRETTY_FUNCTION__);
    
    // TODO save what you need here
    
    [super encodeRestorableStateWithCoder:coder];
}

- (void)decodeRestorableStateWithCoder:(NSCoder *)coder {
    NSLog(@"%s", __PRETTY_FUNCTION__);
    
    // TODO restore what you need here
    
    [super decodeRestorableStateWithCoder:coder];
}

- (void)applicationFinishedRestoringState {
    NSLog(@"%s", __PRETTY_FUNCTION__);
    
    // TODO call whatever function you need to visually restore
}

// -----------------------------------------------------------------------------


@end
