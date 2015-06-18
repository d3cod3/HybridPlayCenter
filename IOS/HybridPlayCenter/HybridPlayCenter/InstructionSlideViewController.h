//
//  InstructionSlideViewController.h
//  HybridPlayCenter
//
//  Created by n3m3da on 9/6/15.
//  Copyright (c) 2015 Apportable. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface InstructionSlideViewController : UIViewController{
    
}

@property (assign, nonatomic) IBOutlet UIImageView *backgroundImageView;

@property (assign) NSUInteger pageIndex;
@property (assign) NSString *imageFile;

@end
