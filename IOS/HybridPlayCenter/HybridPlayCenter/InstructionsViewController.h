//
//  InstructionsViewController.h
//  HybridPlayCenter
//
//  Created by n3m3da on 28/4/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "InstructionSlideViewController.h"

@interface InstructionsViewController : UIViewController <UIPageViewControllerDataSource>{
    
    UIBarButtonItem *revealButtonItem;
    
}

// -----------------------------------------------------------------------------
@property (strong, nonatomic) UIPageViewController *pageViewController;
@property (strong, nonatomic) NSArray *pageImages;


@end
