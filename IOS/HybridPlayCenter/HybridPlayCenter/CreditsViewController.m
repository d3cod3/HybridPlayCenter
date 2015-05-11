//
//  CreditsViewController.m
//  HybridPlayCenter
//
//  Created by n3m3da on 28/4/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import "CreditsViewController.h"
#import "SWRevealViewController.h"

@interface CreditsViewController ()

@end

@implementation CreditsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupNavBar];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setupNavBar{
    SWRevealViewController *revealViewController = self.revealViewController;
    if( revealViewController ){
        
        UIButton *menuButton =  [UIButton buttonWithType:UIButtonTypeCustom];
        [menuButton setImage:[UIImage imageNamed:@"ic_launcher.png"] forState:UIControlStateNormal];
        [menuButton addTarget:self.revealViewController action:@selector( revealToggle: ) forControlEvents:UIControlEventTouchUpInside];
        [menuButton setFrame:CGRectMake(0, 0, 32, 32)];
        
        revealButtonItem = [[UIBarButtonItem alloc] initWithCustomView:menuButton];
        
        NSArray *actionLeftButtonItems = @[revealButtonItem];
        self.navigationItem.leftBarButtonItems = actionLeftButtonItems;
        
        [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    }
    
}

@end
